package org.cyclops.cyclopscore.nbt.path.navigate;

import javax.annotation.Nullable;

/**
 * A navigation inner link that matches with all leafs.
 */
public class NbtPathNavigationLinkWildcard implements INbtPathNavigation {

    private final INbtPathNavigation child;

    public NbtPathNavigationLinkWildcard(INbtPathNavigation child) {
        this.child = child;
    }

    @Override
    public boolean isLeafKey(String key) {
        return false;
    }

    @Nullable
    @Override
    public INbtPathNavigation getNext(String key) {
        return this.child;
    }
}
