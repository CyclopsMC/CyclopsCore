package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * A server-side button action.
 * @author rubensworks
 */
public interface IContainerButtonAction<C extends AbstractContainerMenu> {

    /**
     * Called when the button with the given id was clicked client-side.
     * @param buttonId The button id.
     * @param container The container in which the button was clicked.
     */
    public void onAction(String buttonId, C container);


}
