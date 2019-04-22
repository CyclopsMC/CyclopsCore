package org.cyclops.cyclopscore.nbt.path.navigate;

import javax.annotation.Nullable;

/**
 * Datastructure that represents a concrete key-based navigation path that can be derived from a JSON path.
 */
public interface INbtPathNavigation {

    /**
     * Check if the given key is a leaf in this navigation,
     * i.e., it is present and has no children.
     * @param key A key.
     * @return If it is a leaf key.
     */
    public boolean isLeafKey(String key);

    /**
     * Get the child navigation of the given key, or null if it is not present.
     * @param key A key.
     * @return The child navigation or null.
     */
    @Nullable
    public INbtPathNavigation getNext(String key);

}
