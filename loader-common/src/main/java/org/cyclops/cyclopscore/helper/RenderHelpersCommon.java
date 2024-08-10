package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Random;

/**
 * @author rubensworks
 */
public class RenderHelpersCommon implements IRenderHelpers {

    private static final Random rand = new Random();

    @Override
    public void bindTexture(ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
    }

    @Override
    public void emitParticle(Particle particle) {
        Minecraft.getInstance().particleEngine.add(particle);
    }

    @Override
    public void drawScaledString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);
        guiGraphics.pose().scale(scale, scale, 1.0f);
        fontRenderer.drawInBatch(string, 0, 0, color, shadow, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), displayMode, 0, 15728880);
        guiGraphics.pose().popPose();
    }

    @Override
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color, shadow, displayMode);
    }

    @Override
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        float originalWidth = fontRenderer.width(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, width, scale, color, shadow, displayMode);
    }

    @Override
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.width(string);
        int titleHeight = fontRenderer.lineHeight;
        fontRenderer.drawInBatch(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color, false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        guiGraphics.pose().popPose();
    }

    @Override
    public BakedModel getBakedModel(BlockState blockState) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRendererDispatcher = mc.getBlockRenderer();
        BlockModelShaper blockModelShapes = blockRendererDispatcher.getBlockModelShaper();
        return blockModelShapes.getBlockModel(blockState);
    }

    @Override
    public BakedModel getDynamicBakedModel(Level world, BlockPos pos) {
        return getBakedModel(world.getBlockState(pos));
    }

    @Override
    public void addBlockHitEffects(ParticleEngine particleManager, ClientLevel world, BlockState blockState, BlockPos pos, Direction side) {
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AABB bb = blockState.getShape(world, pos).bounds();
            double d0 = (double)i + rand.nextDouble() * (bb.maxX - bb.minX - (double)(f * 2.0F)) + (double)f + bb.minX;
            double d1 = (double)j + rand.nextDouble() * (bb.maxY - bb.minY - (double)(f * 2.0F)) + (double)f + bb.minY;
            double d2 = (double)k + rand.nextDouble() * (bb.maxZ - bb.minZ - (double)(f * 2.0F)) + (double)f + bb.minZ;

            if (side == Direction.DOWN)  d1 = (double)j + bb.minY - (double)f;
            if (side == Direction.UP)    d1 = (double)j + bb.maxY + (double)f;
            if (side == Direction.NORTH) d2 = (double)k + bb.minZ - (double)f;
            if (side == Direction.SOUTH) d2 = (double)k + bb.maxZ + (double)f;
            if (side == Direction.WEST)  d0 = (double)i + bb.minX - (double)f;
            if (side == Direction.EAST)  d0 = (double)i + bb.maxX + (double)f;

            Particle fx = new TerrainParticle.Provider().createParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), world, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            particleManager.add(fx);
        }
    }

    private static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    @Override
    public Function<ResourceLocation, TextureAtlasSprite> getBlockTextureGetter() {
        return TEXTURE_GETTER;
    }

    @Override
    public TextureAtlasSprite getBlockIcon(Block block) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(block.defaultBlockState());
    }

    @Override
    public boolean isPointInRegion(int left, int top, int width, int height, double pointX, double pointY) {
        return pointX >= left && pointX < left + width && pointY >= top && pointY < top + height;
    }

    @Override
    public boolean isPointInRegion(Rectangle region, Point point) {
        return isPointInRegion(region.x, region.y, region.width, region.height, point.x, point.y);
    }

    @Override
    public boolean isPointInButton(Button button, int pointX, int pointY) {
        return isPointInRegion(button.getX(), button.getY(), button.getWidth(), button.getHeight(), pointX, pointY);
    }

    @Override
    public void blitColored(GuiGraphics guiGraphics, int x, int y, int z, float u, float v, int width, int height, float r, float g, float b, float a) {
        blitColored(guiGraphics, x, y, z, width, height, u / 256, (u + width) / 256, v / 256, (v + height) / 256, r, g, b, a);
    }

    @Override
    public void blitColored(GuiGraphics guiGraphics, int x, int y, int z, int width, int height, float u0, float u1, float v0, float v1, float r, float g, float b, float a) {
// The following should work, but doesn't, so we just apply a shader color instead.
        /*Matrix4f matrix4f = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, (float)x, (float)y + height, (float)z).color(r, g, b, a).uv(u0, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)z).color(r, g, b, a).uv(u1, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x + width, (float)y, (float)z).color(r, g, b, a).uv(u1, v0).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x, (float)y, (float)z).color(r, g, b, a).uv(u0, v0).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);*/

        RenderSystem.setShaderColor(r, g, b, a);

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float)x, (float)y + height, (float)z).setUv(u0, v1);
        bufferbuilder.addVertex(matrix4f, (float)x + width, (float)y + height, (float)z).setUv(u1, v1);
        bufferbuilder.addVertex(matrix4f, (float)x + width, (float)y, (float)z).setUv(u1, v0);
        bufferbuilder.addVertex(matrix4f, (float)x, (float)y, (float)z).setUv(u0, v0);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
