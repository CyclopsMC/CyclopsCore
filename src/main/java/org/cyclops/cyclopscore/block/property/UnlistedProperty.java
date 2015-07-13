package org.cyclops.cyclopscore.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * A generic implementation of {@link IUnlistedProperty} that allows the storage of any kind of object.
 * @author rubensworks
 */
public class UnlistedProperty<V> implements IUnlistedProperty<V> {

    private final String name;
    private final Class<V> type;

    public UnlistedProperty(String name, Class<V> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(V value) {
        return value != null;
    }

    @Override
    public Class<V> getType() {
        return type;
    }

    @Override
    public String valueToString(V value) {
        return value.toString();
    }
}
