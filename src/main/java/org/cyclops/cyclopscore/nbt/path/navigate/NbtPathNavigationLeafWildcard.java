package org.cyclops.cyclopscore.nbt.path.navigate;

import javax.annotation.Nullable;

/**
 * A navigation leaf that matches with all keys.
 */
public class NbtPathNavigationLeafWildcard implements INbtPathNavigation {

    public static NbtPathNavigationLeafWildcard INSTANCE = new NbtPathNavigationLeafWildcard();

    private NbtPathNavigationLeafWildcard() {

    }

    @Override
    public boolean isLeafKey(String key) {
        return true;
    }

    @Nullable
    @Override
    public INbtPathNavigation getNext(String key) {
        return null;
    }
}
