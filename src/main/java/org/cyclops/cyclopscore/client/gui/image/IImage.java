package org.cyclops.cyclopscore.client.gui.image;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * Interface for graphics objects that can be rendered.
 * @author rubensworks
 */
public interface IImage {

    /**
     * Draw this image.
     * @param gui The gui helper object.
     * @param matrixStack The matrix stack.
     * @param x The x position.
     * @param y The y position.
     */
    public void draw(GuiComponent gui, PoseStack matrixStack, int x, int y);

    /**
     * Draw this image.
     * @param gui The gui helper object.
     * @param matrixStack The matrix stack.
     * @param x The x position.
     * @param y The y position.
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha
     */
    public void drawWithColor(GuiComponent gui, PoseStack matrixStack, int x, int y, float r, float g, float b, float a);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param z Z
     */
    public default void drawWorld(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                  int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2, float z) {
        drawWorldWithAlpha(textureManager, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, x1, x2, y1, y2, z, 1);
    }

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     */
    public default void drawWorld(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                  int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2) {
        drawWorldWithAlpha(textureManager, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, x1, x2, y1, y2, 1);
    }

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x2 End X
     * @param y2 End Y
     */
    public default void drawWorld(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                  int combinedLight, int combinedOverlay, float x2, float y2) {
        drawWorldWithAlpha(textureManager, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, x2, y2, 1);
    }

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param alpha The alpha to render with
     */
    public default void drawWorldWithAlpha(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                           int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2, float alpha) {
        this.drawWorldWithAlpha(textureManager, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, x1, x2, y1, y2, 0, alpha);
    }

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x2 End X
     * @param y2 End Y
     * @param alpha The alpha to render with
     */
    public default void drawWorldWithAlpha(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                           int combinedLight, int combinedOverlay, float x2, float y2, float alpha) {
        this.drawWorldWithAlpha(textureManager, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, 0, x2, 0, y2, alpha);
    }
    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param matrixStack The matrix stack
     * @param renderTypeBuffer The render type buffer
     * @param combinedLight The combined light
     * @param combinedOverlay The combined overlay
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param z Z
     * @param alpha The alpha to render with
     */
    public void drawWorldWithAlpha(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                   int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2, float z, float alpha);

    /**
     * @return The width in pixels.
     */
    public int getWidth();

    /**
     * @return The height in pixels.
     */
    public int getHeight();

}
