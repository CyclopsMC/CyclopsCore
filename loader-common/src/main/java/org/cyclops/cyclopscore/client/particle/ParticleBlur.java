package org.cyclops.cyclopscore.client.particle;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
public class ParticleBlur extends SingleQuadParticle {

    public static final RenderPipeline RENDER_PIPELINE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET) // Modified from RenderPipelines.TRANSLUCENT_PARTICLE
            .withLocation("pipeline/translucent_particle_blur")
            .withBlend(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
            .build();
    // TODO: make the particle blurry again. The key here is in BlurTexturingStateShard. May work using a new ParticleGroup.
//    public static final RenderType RENDER_TYPE = RenderType.create( // Modified from RenderType.TRANSLUCENT_PARTICLE
//            Reference.MOD_ID + ":blur",
//            1536,
//            false,
//            false,
//            RENDER_PIPELINE,
//            RenderType.CompositeState.builder()
//                    .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_PARTICLES, false))
//                    .setTexturingState(new BlurTexturingStateShard(TextureAtlas.LOCATION_PARTICLES))
//                    .setOutputState(RenderType.PARTICLES_TARGET)
//                    .setLightmapState(RenderType.LIGHTMAP)
//                    .createCompositeState(false)
//    );
//    public static final ParticleRenderType PARTICLE_RENDER_TYPE = new ParticleRenderType(Reference.MOD_ID + ":blur");
    public static final SingleQuadParticle.Layer LAYER = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, RENDER_PIPELINE);

    private static final int MAX_VIEW_DISTANCE = 30;

    protected float originalScale;
    protected float scaleLife;

    public ParticleBlur(ParticleBlurData data, ClientLevel world, double x, double y, double z,
                        double motionX, double motionY, double motionZ, TextureAtlasSprite sprite) {
        super(world, x, y, z, motionX, motionY, motionZ, sprite);
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

        if(Minecraft.getInstance().options.graphicsPreset().get().ordinal() == 0) {
            visibleDistance = visibleDistance / 2;
        }

        if(renderentity == null
                || renderentity.distanceToSqr(x, y, z) > visibleDistance * visibleDistance) {
            lifetime = 0;
        }
    }

    @Override
    protected Layer getLayer() {
        return LAYER;
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
}
