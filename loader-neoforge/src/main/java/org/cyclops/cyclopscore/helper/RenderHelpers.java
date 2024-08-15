package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.util.Random;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@Deprecated // TODO: remove in next major version
@OnlyIn(Dist.CLIENT)
public class RenderHelpers {

    private static final Random rand = new Random();
    public static final int SLOT_SIZE = 16;

    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
        IModHelpers.get().getRenderHelpers().bindTexture(texture);
    }

    /**
     * Add a particle to the world.
     * @param particle A particle.
     */
    public static void emitParticle(Particle particle) {
        IModHelpers.get().getRenderHelpers().emitParticle(particle);
    }

    /**
     * Draw the given text with the given scale with a shadow.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public static void drawScaledString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        IModHelpers.get().getRenderHelpers().drawScaledString(guiGraphics, fontRenderer, string, x, y, scale, color, shadow, displayMode);
    }

    /**
     * Draw the given text with the given scale with a shadow.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param multiBufferSource The buffer source
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    @Deprecated // TODO: RM in next major MC version
    public static void drawScaledString(PoseStack matrixStack, MultiBufferSource multiBufferSource, Font fontRenderer, String string, int x, int y, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, 1.0f);
        fontRenderer.drawInBatch(string, 0, 0, color, shadow, matrixStack.last().pose(), multiBufferSource, displayMode, 0, 15728880);
        matrixStack.popPose();
    }

    /**
     * Draw the given text and scale it to the max width.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public static void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        IModHelpers.get().getRenderHelpers().drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, maxWidth, color, shadow, displayMode);
    }

    /**
     * Draw the given text and scale it to the max width.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param multiBufferSource The buffer source
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    @Deprecated // TODO: RM in next major MC version
    public static void drawScaledCenteredString(PoseStack matrixStack, MultiBufferSource multiBufferSource, Font fontRenderer, String string, int x, int y, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        drawScaledCenteredString(matrixStack, multiBufferSource, fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color, shadow, displayMode);
    }

    /**
     * Draw the given text and scale it to the max width.
     * The given string may already be scaled and its width must be passed in that case.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param originalScale The original scale
     * @param maxWidth The maximum width to scale to
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public static void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        IModHelpers.get().getRenderHelpers().drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, width, originalScale, maxWidth, color, shadow, displayMode);
    }

    /**
     * Draw the given text and scale it to the max width.
     * The given string may already be scaled and its width must be passed in that case.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param multiBufferSource The buffer source
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param originalScale The original scale
     * @param maxWidth The maximum width to scale to
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    @Deprecated // TODO: RM in next major MC version
    public static void drawScaledCenteredString(PoseStack matrixStack, MultiBufferSource multiBufferSource, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode) {
        float originalWidth = fontRenderer.width(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(matrixStack, multiBufferSource, fontRenderer, string, x, y, width, scale, color, shadow, displayMode);
    }

    /**
     * Draw the given text with the given width and desired scale.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param scale The desired scale
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The font display mode
     */
    public static void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        IModHelpers.get().getRenderHelpers().drawScaledCenteredString(guiGraphics, fontRenderer, string, x, y, width, scale, color, shadow, displayMode);
    }

    /**
     * Draw the given text with the given width and desired scale.
     * @param matrixStack The matrix stack
     * @param multiBufferSource The buffer source
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param scale The desired scale
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The font display mode
     */
    @Deprecated // TODO: RM in next major MC version
    public static void drawScaledCenteredString(PoseStack matrixStack, MultiBufferSource multiBufferSource, Font fontRenderer, String string, int x, int y, int width, float scale, int color, boolean shadow, Font.DisplayMode displayMode) {
        matrixStack.pushPose();
        matrixStack.scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.width(string);
        int titleHeight = fontRenderer.lineHeight;
        fontRenderer.drawInBatch(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color, false, matrixStack.last().pose(), multiBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        matrixStack.popPose();
    }

    /**
     * Retrieve the baked model from a given block state.
     * @param blockState The block state.
     * @return The corresponding baked model.
     */
    public static BakedModel getBakedModel(BlockState blockState) {
        return IModHelpers.get().getRenderHelpers().getBakedModel(blockState);
    }

    /**
     * Retrieve the potentially dynamic baked model for a position.
     * This will automatically take into account smart block models.
     * @param world The world.
     * @param pos The position.
     * @return The baked model.
     */

    public static BakedModel getDynamicBakedModel(Level world, BlockPos pos) {
        return IModHelpers.get().getRenderHelpers().getDynamicBakedModel(world, pos);
    }

    /**
     * A custom way of spawning block hit effects.
     * @param particleManager The effect renderer.
     * @param world The world.
     * @param blockState The blockstate to render particles for.
     * @param pos The position.
     * @param side The hit side.
     */
    public static void addBlockHitEffects(ParticleEngine particleManager, ClientLevel world, BlockState blockState, BlockPos pos, Direction side)  {
        IModHelpers.get().getRenderHelpers().addBlockHitEffects(particleManager, world, blockState, pos, side);
    }

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);

    /**
     * Get the default icon from a block.
     * @param block The block.
     * @return The icon.
     */
    public static TextureAtlasSprite getBlockIcon(Block block) {
        return IModHelpers.get().getRenderHelpers().getBlockIcon(block);
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side) {
        return IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluid, side);
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid stack.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, Direction side) {
        return IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluid, side);
    }

    /**
     * Prepare a render system context for rendering fluids.
     * @param fluid The fluid stack.
     * @param matrixStack The matrix stack.
     * @param render The actual fluid renderer.
     */
    public static void renderFluidContext(FluidStack fluid, PoseStack matrixStack, IFluidContextRender render) {
        IModHelpersNeoForge.get().getRenderHelpers().renderFluidContext(fluid, matrixStack, render::render);
    }

    /**
     * Get the fluid color to use in buffer rendering.
     * @param fluidStack The fluid stack.
     * @return The RGB colors.
     */
    public static Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack) {
        return IModHelpersNeoForge.get().getRenderHelpers().getFluidVertexBufferColor(fluidStack);
    }

    /**
     * Get the fluid color to use in a baked quad.
     * @param fluidStack The fluid stack.
     * @return The BGR colors.
     */
    public static int getFluidBakedQuadColor(FluidStack fluidStack) {
        return IModHelpersNeoForge.get().getRenderHelpers().getFluidBakedQuadColor(fluidStack);
    }

    /**
     * Check if a point is inside a region.
     * @param left Left-top corner x
     * @param top Left-top corner y
     * @param width The width
     * @param height The height
     * @param pointX The point x
     * @param pointY The point y
     * @return If the point is inside the region.
     */
    public static boolean isPointInRegion(int left, int top, int width, int height, double pointX, double pointY) {
        return IModHelpersNeoForge.get().getRenderHelpers().isPointInRegion(left, top, width, height, pointX, pointY);
    }

    /**
     * Check if a point is inside a region.
     * @param region The region.
     * @param point The point.
     * @return If the point is inside the region.
     */
    public static boolean isPointInRegion(Rectangle region, Point point) {
        return IModHelpersNeoForge.get().getRenderHelpers().isPointInRegion(region, point);
    }

    /**
     * Check if a point is inside a button's region.
     * @param button The button.
     * @param pointX The point x
     * @param pointY The point y
     * @return If the point is inside the button's region.
     */
    public static boolean isPointInButton(Button button, int pointX, int pointY) {
        return IModHelpersNeoForge.get().getRenderHelpers().isPointInButton(button, pointX, pointY);
    }

    public static void blitColored(GuiGraphics guiGraphics, int x, int y, int z, float u, float v, int width, int height, float r, float g, float b, float a) {
        IModHelpersNeoForge.get().getRenderHelpers().blitColored(guiGraphics, x, y, z, u, v, width, height, r, g, b, a);
    }

    public static void blitColored(GuiGraphics guiGraphics, int x, int y, int z, int width, int height, float u0, float u1, float v0, float v1, float r, float g, float b, float a) {
        IModHelpersNeoForge.get().getRenderHelpers().blitColored(guiGraphics, x, y, z, width, height, u0, u1, v0, v1, r, g, b, a);
    }

    /**
     * Runnable for {@link RenderHelpers#renderFluidContext(FluidStack, PoseStack, IFluidContextRender)}}}.
     * @author rubensworks
     */
    public static interface IFluidContextRender {

        /**
         * Render the fluid.
         */
        public void render();

    }

}
