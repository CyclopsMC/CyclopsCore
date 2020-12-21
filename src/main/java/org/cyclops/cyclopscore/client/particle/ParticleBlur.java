package org.cyclops.cyclopscore.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
public class ParticleBlur extends SpriteTexturedParticle {

	private static final int MAX_VIEW_DISTANCE = 30;
	private static final RenderType RENDER_TYPE = new RenderType();

	protected float originalScale;
	protected float scaleLife;

	public ParticleBlur(ParticleBlurData data, ClientWorld world, double x, double y, double z,
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
		
		if(Minecraft.getInstance().gameSettings.graphicFanciness.func_238162_a_() == 0) {
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

	@Override
	protected int getBrightnessForRender(float partialTicks) {
		return 0xF000F0;
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
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			RenderSystem.disableLighting();

			textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
			textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).setBlurMipmap(true, false);

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
		}
	}

}
