package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.client.gui.components.Button;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * A container button click acceptor.
 * Events will be received both server-side and client-side.
 *
 * This will only work if the client-side button's pressable has been generated with
 * {@link org.cyclops.cyclopscore.client.gui.container.ContainerScreenExtended#createServerPressable(String, Button.OnPress)}}.
 *
 * @author rubensworks
 */
public interface IContainerButtonClickAcceptorServer<C extends AbstractContainerMenu> {

    /**
     * Set a button action.
     *
     * The used button id should correspond to id that was used to create the button's pressable in the gui.
     *
     * @param buttonId The button id.
     * @param action The action to set for the given button id.
     */
    public void putButtonAction(String buttonId, IContainerButtonAction<C> action);

    /**
     * When the button is clicked, both server and client side.
     * @param buttonId The button id.
     * @return If an action was found and executed.
     */
    public boolean onButtonClick(String buttonId);

}
