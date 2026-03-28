package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.awt.*;
import java.util.Random;

/**
 * @author rubensworks
 */
public class RenderHelpersCommon implements IRenderHelpers {

    private static final Random rand = new Random();

    @Override
    public void emitParticle(Particle particle) {
        Minecraft.getInstance().particleEngine.add(particle);
    }

    @Override
    public void drawScaledString(GuiGraphicsExtractor guiGraphics, Font fontRenderer, String string, int x, int y, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.text(fontRenderer, string, 0, 0, color, shadow);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public void drawScaledCenteredString(GuiGraphicsExtractor guiGraphics, Font fontRenderer, String string, int x, int y, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color, shadow, displayMode);
    }

    @Override
    public void drawScaledCenteredString(GuiGraphicsExtractor guiGraphics, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        float originalWidth = fontRenderer.width(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, width, scale, color, shadow, displayMode);
    }

    @Override
    public void drawScaledCenteredString(GuiGraphicsExtractor guiGraphics, Font fontRenderer, String string, int x, int y, int width, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(scale, scale);
        int titleLength = fontRenderer.width(string);
        int titleHeight = fontRenderer.lineHeight;
        guiGraphics.text(fontRenderer, string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color, false);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public BlockStateModel getBakedModel(BlockState blockState) {
        BlockStateModelSet blockModelSet = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
        return blockModelSet.get(blockState);
    }

    @Override
    public BlockStateModel getDynamicBakedModel(Level world, BlockPos pos) {
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

            particleManager.add(particleManager.createParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d0, d1, d2, 0.0D, 0.0D, 0.0D));
        }
    }

    private static final Function<Identifier, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(Identifier.fromNamespaceAndPath("minecraft", "blocks")).getSprite(location);
    @Override
    public Function<Identifier, TextureAtlasSprite> getBlockTextureGetter() {
        return TEXTURE_GETTER;
    }

    @Override
    public TextureAtlasSprite getBlockIcon(Block block) {
        return Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(block.defaultBlockState()).sprite();
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
    public void blitColored(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, float u, float v, int width, int height, float r, float g, float b, float a) {
        blitColored(guiGraphics, texture, x, y, u, v, width, height, 256, 256, r, g, b, a);
    }

    @Override
    public void blitColored(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, float r, float g, float b, float a) {
        int color = ARGB.colorFromFloat(a, r, g, b);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    // Derived from LeverRenderer#renderLineBox
    @Override
    public void renderLineBox(PoseStack poseStack, VertexConsumer buffer, AABB box, float red, float green, float blue, float alpha, float width) {
        renderLineBox(
                poseStack,
                buffer,
                box.minX,
                box.minY,
                box.minZ,
                box.maxX,
                box.maxY,
                box.maxZ,
                red,
                green,
                blue,
                alpha,
                red,
                green,
                blue,
                width
        );
    }

    @Override
    public void renderLineBox(
            PoseStack poseStack,
            VertexConsumer consumer,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            float red,
            float green,
            float blue,
            float alpha,
            float red2,
            float green2,
            float blue2,
            float width) {
        PoseStack.Pose posestack$pose = poseStack.last();
        float f = (float)minX;
        float f1 = (float)minY;
        float f2 = (float)minZ;
        float f3 = (float)maxX;
        float f4 = (float)maxY;
        float f5 = (float)maxZ;
        consumer.addVertex(posestack$pose, f, f1, f2).setColor(red, green2, blue2, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f1, f2).setColor(red, green2, blue2, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f1, f2).setColor(red2, green, blue2, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f4, f2).setColor(red2, green, blue2, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f1, f2).setColor(red2, green2, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
        consumer.addVertex(posestack$pose, f, f1, f5).setColor(red2, green2, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
        consumer.addVertex(posestack$pose, f3, f1, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f4, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f4, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, -1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f4, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, -1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f4, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
        consumer.addVertex(posestack$pose, f, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
        consumer.addVertex(posestack$pose, f, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, -1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f1, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, -1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f, f1, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f1, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f1, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, -1.0F);
        consumer.addVertex(posestack$pose, f3, f1, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, -1.0F);
        consumer.addVertex(posestack$pose, f, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 1.0F, 0.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f1, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(posestack$pose, f3, f4, f2).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
        consumer.addVertex(posestack$pose, f3, f4, f5).setColor(red, green, blue, alpha).setLineWidth(width).setNormal(posestack$pose, 0.0F, 0.0F, 1.0F);
    }
}
