package org.cyclops.cyclopscore.client.particle;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.lwjgl.opengl.GL11;

import java.util.Queue;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ParticleBlur extends Particle {

	private static final Queue<ParticleBlur> BLURS_PENDING_RENDER = Queues.newLinkedBlockingDeque();
	private static final ResourceLocation TEXTURE = new ResourceLocation(
			Reference.MOD_ID, Reference.TEXTURE_PATH_PARTICLES + "particle_blur.png");
	private static final int MAX_VIEW_DISTANCE = 30;
	
	private float scaleLife;
	private float originalScale;

	// Last params from renderParticle
	private float f;
	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;

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
    public void renderParticle(BufferBuilder worldRenderer, ActiveRenderInfo renderInfo,
							   float f, float f1, float f2, float f3, float f4, float f5) {
		this.f = f;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;
		BLURS_PENDING_RENDER.add(this);
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.CUSTOM;
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

	private void renderQueued(BufferBuilder worldRenderer) {
		float agescale = (float)age / (float) scaleLife;
		if(agescale > 1F) {
			agescale = 2 - agescale;
		}

		float currentScale = originalScale * agescale;

		float f10 = 0.5F * currentScale;
		float f11 = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);

		int i = this.getBrightnessForRender(f5);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		worldRenderer.pos(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10).tex(0, 1).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
		worldRenderer.pos(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10).tex(1, 1).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
		worldRenderer.pos(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10).tex(1, 0).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
		worldRenderer.pos(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10).tex(0, 0).
				color(particleRed, particleGreen, particleBlue, 0.9F).lightmap(j, k).endVertex();
	}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		if (!BLURS_PENDING_RENDER.isEmpty()) {
			GlStateManager.pushMatrix();

			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GlStateManager.disableLighting();

			RenderHelpers.bindTexture(TEXTURE);

			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 0.75F);

			BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

			ParticleBlur particleBlur;
			while ((particleBlur = BLURS_PENDING_RENDER.poll()) != null) {
				particleBlur.renderQueued(worldRenderer);
			}

			Tessellator.getInstance().draw();

			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GlStateManager.popMatrix();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1F);
			GL11.glPopAttrib();
		}
	}

}
