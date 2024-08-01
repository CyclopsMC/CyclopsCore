package org.cyclops.cyclopscore.nbt.path.navigate;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A navigation that takes the union of several navigations.
 */
public class NbtPathNavigationList implements INbtPathNavigation {

    private final List<INbtPathNavigation> navigations;

    public NbtPathNavigationList(List<INbtPathNavigation> navigations) {
        this.navigations = navigations;
    }

    @Override
    public boolean isLeafKey(String key) {
        for (INbtPathNavigation navigation : this.navigations) {
            if (navigation.isLeafKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public INbtPathNavigation getNext(String key) {
        List<INbtPathNavigation> subNavigations = Lists.newArrayList();
        for (INbtPathNavigation navigation : this.navigations) {
            INbtPathNavigation subNavigation = navigation.getNext(key);
            if (subNavigation != null) {
                subNavigations.add(subNavigation);
            }
        }
        if (subNavigations.isEmpty()) {
            return null;
        }
        return new NbtPathNavigationList(subNavigations);
    }
}
