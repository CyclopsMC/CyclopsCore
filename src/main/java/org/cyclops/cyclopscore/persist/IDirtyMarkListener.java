package org.cyclops.cyclopscore.persist;

/**
 * Interface that is used to listen to dirty markings.
 * @author rubensworks
 */
public interface IDirtyMarkListener {

    /**
     * Called when the target is marked as dirty.
     */
    public void onDirty();

}
