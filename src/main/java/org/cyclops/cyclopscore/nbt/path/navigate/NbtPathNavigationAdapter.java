package org.cyclops.cyclopscore.nbt.path.navigate;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * A base navigation implementation.
 */
public class NbtPathNavigationAdapter implements INbtPathNavigation {

    private final Collection<String> keys;
    @Nullable
    private final INbtPathNavigation child;

    public NbtPathNavigationAdapter(Collection<String> keys, @Nullable INbtPathNavigation child) {
        this.keys = keys;
        this.child = child;
    }

    public NbtPathNavigationAdapter(String key, @Nullable INbtPathNavigation child) {
        this(Collections.singleton(key), child);
    }

    protected boolean isLeaf() {
        return this.child == null;
    }

    @Override
    public boolean isLeafKey(String key) {
        return isLeaf() && this.keys.contains(key);
    }

    @Nullable
    @Override
    public INbtPathNavigation getNext(String key) {
        return !isLeaf() && this.keys.contains(key) ? child : null;
    }

}
