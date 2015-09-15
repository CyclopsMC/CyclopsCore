package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.inventory.Container;

/**
 * A button action interface for the server.
 * @author rubensworks
 */
public interface IButtonActionServer<C extends Container> extends IButtonAction<C> {

    /**
     * Called when clicked.
     * @param buttonId The button id.
     * @param container The container in which the button was clicked.
     */
    public void onAction(int buttonId, C container);

}
