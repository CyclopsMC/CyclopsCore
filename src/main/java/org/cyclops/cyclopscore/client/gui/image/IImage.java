package org.cyclops.cyclopscore.client.gui.image;

import net.minecraft.client.gui.Gui;

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

}
