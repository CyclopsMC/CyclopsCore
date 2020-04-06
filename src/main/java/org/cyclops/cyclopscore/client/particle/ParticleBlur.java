package org.cyclops.cyclopscore.client.particle;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.Reference;
import org.lwjgl.opengl.GL11;

import java.util.Queue;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
public class ParticleBlur extends SpriteTexturedParticle {

	private static final Queue<ParticleBlur> BLURS_PENDING_RENDER = Queues.newLinkedBlockingDeque();
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "particle_blur.png");
	private static final int MAX_VIEW_DISTANCE = 30;
	private static final RenderType RENDER_TYPE = new RenderType();

	protected float originalScale;
	protected float scaleLife;

	public ParticleBlur(ParticleBlurData data, World world, double x, double y, double z,
						double motionX, double motionY, double motionZ) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		
		this.particleRed = data.getRed();
		this.particleGreen = data.getGreen();
		this.particleBlue = data.getBlue();
		this.particleAlpha = 0.9F;
		this.particleGravity = 0;
		
		this.originalScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F * data.getScale();
		this.maxAge = (int) ((rand.nextFloat() * 0.33F + 0.66F) * data.getAgeMultiplier());
		this.setSize(0.01F, 0.01F);
		
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		
		this.scaleLife = (float) (maxAge / 2.5);
		
		validateDistance();
	}
	
	private void validateDistance() {
		LivingEntity renderentity = Minecraft.getInstance().player;
		int visibleDistance = MAX_VIEW_DISTANCE;
		
		if(!Minecraft.getInstance().gameSettings.fancyGraphics) {
			visibleDistance = visibleDistance / 2;
		}

		if(renderentity == null
				|| renderentity.getDistanceSq(posX, posY, posZ) > visibleDistance * visibleDistance) {
			maxAge = 0;
		}
	}

	@Override
	public IParticleRenderType getRenderType() {
		return RENDER_TYPE;
	}

	@Override
	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (age++ >= maxAge) {
			setExpired();
		}

		motionY -= 0.04D * particleGravity;
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.particleGravity = particleGravity;
	}

	@Override
	public float getScale(float p_217561_1_) {
		float agescale = age / this.scaleLife;
		if (agescale > 1F) {
			agescale = 2 - agescale;
		}
		particleScale = originalScale * agescale * 0.5F;
		return particleScale;
	}

	public static class RenderType implements IParticleRenderType {

		@Override
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			RenderSystem.pushMatrix();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.disableLighting();

			textureManager.bindTexture(TEXTURE);

			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.75F);

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();

			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			RenderSystem.popMatrix();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
			GL11.glPopAttrib();
		}
	}

}
