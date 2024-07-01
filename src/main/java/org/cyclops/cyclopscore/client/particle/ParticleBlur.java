package org.cyclops.cyclopscore.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.LivingEntity;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/**
 * A blurred static fading particle with any possible color.
 * @author rubensworks
 *
 */
public class ParticleBlur extends TextureSheetParticle {

    private static final int MAX_VIEW_DISTANCE = 30;
    private static final RenderType RENDER_TYPE = new RenderType();

    protected float originalScale;
    protected float scaleLife;

    public ParticleBlur(ParticleBlurData data, ClientLevel world, double x, double y, double z,
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

        if(Minecraft.getInstance().options.graphicsMode().get().getId() == 0) {
            visibleDistance = visibleDistance / 2;
        }

        if(renderentity == null
                || renderentity.distanceToSqr(x, y, z) > visibleDistance * visibleDistance) {
            lifetime = 0;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
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

    public static class RenderType implements ParticleRenderType {

        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            //RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
            //RenderSystem.disableLighting();

            RenderHelpers.bindTexture(TextureAtlas.LOCATION_PARTICLES);
            textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);

            return new BufferBuilderWrapper(tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE), this::end);
        }

        public void end() {
            Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
            //RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    }

    public static class BufferBuilderWrapper extends BufferBuilder {
        private final BufferBuilder bufferBuilder;
        private final Runnable onBuild;

        public BufferBuilderWrapper(BufferBuilder bufferBuilder, Runnable onBuild) {
            super(null, null, null);
            this.bufferBuilder = bufferBuilder;
            this.onBuild = onBuild;
        }

        @Nullable
        @Override
        public MeshData build() {
            MeshData ret = this.bufferBuilder.build();
            this.onBuild.run();
            return ret;
        }

        @Override
        public VertexConsumer addVertex(float p_350828_, float p_350614_, float p_350700_) {
            return this.bufferBuilder.addVertex(p_350828_, p_350614_, p_350700_);
        }

        @Override
        public VertexConsumer setColor(int p_350581_, int p_350952_, int p_350275_, int p_350985_) {
            return this.bufferBuilder.setColor(p_350581_, p_350952_, p_350275_, p_350985_);
        }

        @Override
        public VertexConsumer setColor(int p_350530_) {
            return this.bufferBuilder.setColor(p_350530_);
        }

        @Override
        public VertexConsumer setUv(float p_350574_, float p_350773_) {
            return this.bufferBuilder.setUv(p_350574_, p_350773_);
        }

        @Override
        public VertexConsumer setUv1(int p_350396_, int p_350722_) {
            return this.bufferBuilder.setUv1(p_350396_, p_350722_);
        }

        @Override
        public VertexConsumer setOverlay(int p_350297_) {
            return this.bufferBuilder.setOverlay(p_350297_);
        }

        @Override
        public VertexConsumer setUv2(int p_351058_, int p_350320_) {
            return this.bufferBuilder.setUv2(p_351058_, p_350320_);
        }

        @Override
        public VertexConsumer setLight(int p_350848_) {
            return this.bufferBuilder.setLight(p_350848_);
        }

        @Override
        public VertexConsumer setNormal(float p_351000_, float p_350982_, float p_350974_) {
            return this.bufferBuilder.setNormal(p_351000_, p_350982_, p_350974_);
        }

        @Override
        public void addVertex(float p_350423_, float p_350381_, float p_350383_, int p_350371_, float p_350977_, float p_350674_, int p_350816_, int p_350690_, float p_350640_, float p_350490_, float p_350810_) {
            this.bufferBuilder.addVertex(p_350423_, p_350381_, p_350383_, p_350371_, p_350977_, p_350674_, p_350816_, p_350690_, p_350640_, p_350490_, p_350810_);
        }
    }

}
