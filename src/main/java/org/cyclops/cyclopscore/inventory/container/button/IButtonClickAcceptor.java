package org.cyclops.cyclopscore.inventory.container.button;

/**
 * Interface for container that can accept button clicks.
 * This will also be automatically be sent to the server.
 * @author rubensworks
 */
public interface IButtonClickAcceptor<A extends IButtonAction> {

    /**
     * Set a button action.
     * @param buttonId The button id.
     * @param action The action to set for the given button id.
     */
    public void putButtonAction(int buttonId, A action);

    /**
     * If the given button action should be sent to this.
     * @param buttonId The button id.
     * @return If it should be notified.
     */
    public boolean requiresAction(int buttonId);

    /**
     * When the button is clicked, both server and client side.
     * @param buttonId The button id.
     */
    public void onButtonClick(int buttonId);

}
