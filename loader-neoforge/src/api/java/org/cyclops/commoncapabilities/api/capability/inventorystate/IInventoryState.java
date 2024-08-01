package org.cyclops.commoncapabilities.api.capability.inventorystate;

/**
 * Represents the current state of an inventory.
 * @author rubensworks
 */
public interface IInventoryState {

    /**
     * Get a state value which represents the current state of an inventory.
     * This method must be able to calculate the state very quickly.
     * Ideally, this should be pre-calculated.
     *
     * If inventory contents change, this method is guaranteed to return a different result.
     * A different state does however not necessarily guarantee an inventory change
     * but it should in most cases since callers might gate expensive logic behind hash changes.
     *
     * @return A value representing the current state of an inventory.
     */
    public int getState();

}
