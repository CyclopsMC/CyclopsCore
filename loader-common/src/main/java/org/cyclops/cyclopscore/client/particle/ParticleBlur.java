package org.cyclops.cyclopscore.client.particle;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.cyclopscore.Reference;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
public class ParticleBlur extends SingleQuadParticle {

    public static final RenderPipeline.Snippet PARTICLE_SNIPPET_BLUR = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET) // Modified from RenderPipelines.PARTICLE_SNIPPET
            .withVertexShader("core/particle")
//            .withFragmentShader("core/particle") // Was this
            .withFragmentShader(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "core/particle_blur"))
            .withSampler("Sampler0")
            .withSampler("Sampler2")
            .withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
            .buildSnippet();
    public static final RenderPipeline RENDER_PIPELINE = RenderPipeline.builder(PARTICLE_SNIPPET_BLUR) // Modified from RenderPipelines.TRANSLUCENT_PARTICLE
            .withLocation(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "pipeline/translucent_particle_blur"))
            .withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE)))
            .build();
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
    protected int getLightCoords(float partialTicks) {
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
