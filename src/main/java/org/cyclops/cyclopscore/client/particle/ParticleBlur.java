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
		this.xd = motionX;
		this.yd = motionY;
		this.zd = motionZ;
		
		this.rCol = data.getRed();
		this.gCol = data.getGreen();
		this.bCol = data.getBlue();
		this.alpha = 0.9F;
		this.gravity = 0;
		
		this.originalScale = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F * data.getScale();
		this.lifetime = (int) ((random.nextFloat() * 0.33F + 0.66F) * data.getAgeMultiplier());
		this.setSize(0.01F, 0.01F);
		
		this.xo = x;
		this.yo = y;
		this.zo = z;
		
		this.scaleLife = (float) (lifetime / 2.5);
		
		validateDistance();
	}

	private void validateDistance() {
		LivingEntity renderentity = Minecraft.getInstance().player;
		int visibleDistance = MAX_VIEW_DISTANCE;
		
		if(Minecraft.getInstance().options.graphicsMode.getId() == 0) {
			visibleDistance = visibleDistance / 2;
		}

		if(renderentity == null
				|| renderentity.distanceToSqr(x, y, z) > visibleDistance * visibleDistance) {
			lifetime = 0;
		}
	}

	@Override
	public IParticleRenderType getRenderType() {
		return RENDER_TYPE;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;

		if (age++ >= lifetime) {
			remove();
		}

		yd -= 0.04D * gravity;
		x += xd;
		y += yd;
		z += zd;
		xd *= 0.98000001907348633D;
		yd *= 0.98000001907348633D;
		zd *= 0.98000001907348633D;
	}

	@Override
	protected int getLightColor(float partialTicks) {
		return 0xF000F0;
	}

	/**
	 * Set the gravity for this particle.
	 * @param particleGravity The new gravity
	 */
	public void setGravity(float particleGravity) {
		this.gravity = particleGravity;
	}

	@Override
	public float getQuadSize(float p_217561_1_) {
		float agescale = age / this.scaleLife;
		if (agescale > 1F) {
			agescale = 2 - agescale;
		}
		quadSize = originalScale * agescale * 0.5F;
		return quadSize;
	}

	public static class RenderType implements IParticleRenderType {

		@Override
		public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			RenderSystem.disableLighting();

			textureManager.bind(AtlasTexture.LOCATION_PARTICLES);
			textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES).setBlurMipmap(true, false);

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE);
		}

		@Override
		public void end(Tessellator tessellator) {
			tessellator.end();
			Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES).restoreLastBlurMipmap();
			RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
		}
	}

}
