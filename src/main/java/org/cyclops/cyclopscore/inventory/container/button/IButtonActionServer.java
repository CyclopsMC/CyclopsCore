package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A button action interface for the server.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public interface IButtonActionServer<C extends Container> extends IButtonAction<C> {

    /**
     * Called when clicked.
     * @param buttonId The button id.
     * @param container The container in which the button was clicked.
     */
    public void onAction(int buttonId, C container);

}
