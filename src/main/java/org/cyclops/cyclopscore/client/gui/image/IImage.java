package org.cyclops.cyclopscore.client.gui.image;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * Interface for graphics objects that can be rendered.
 * @author rubensworks
 */
public interface IImage {

    /**
     * Draw this image.
     * @param gui The gui helper object.
     * @param x The x position.
     * @param y The y position.
     */
    public void draw(Gui gui, int x, int y);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param z Z
     */
    public void drawWorld(TextureManager textureManager, float x1, float x2, float y1, float y2, float z);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     */
    public void drawWorld(TextureManager textureManager, float x1, float x2, float y1, float y2);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x2 End X
     * @param y2 End Y
     */
    public void drawWorld(TextureManager textureManager, float x2, float y2);
    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param z Z
     * @param alpha The alpha to render with
     */
    public void drawWorldWithAlpha(TextureManager textureManager, float x1, float x2, float y1, float y2, float z, float alpha);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x1 Start X
     * @param x2 End X
     * @param y1 Start Y
     * @param y2 End Y
     * @param alpha The alpha to render with
     */
    public void drawWorldWithAlpha(TextureManager textureManager, float x1, float x2, float y1, float y2, float alpha);

    /**
     * Draw the image in the world.
     * @param textureManager The texture manager.
     * @param x2 End X
     * @param y2 End Y
     * @param alpha The alpha to render with
     */
    public void drawWorldWithAlpha(TextureManager textureManager, float x2, float y2, float alpha);

    /**
     * @return The width in pixels.
     */
    public int getWidth();

    /**
     * @return The height in pixels.
     */
    public int getHeight();

}
