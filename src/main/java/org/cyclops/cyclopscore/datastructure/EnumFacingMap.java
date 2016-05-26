package org.cyclops.cyclopscore.datastructure;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;
import java.util.Map;

/**
 * An efficient implementation for mapping facings to any value.
 * @author rubensworks
 * @param <V> The value type.
 */
public class EnumFacingMap<V> extends EnumMap<EnumFacing, V> {
    public EnumFacingMap() {
        super(EnumFacing.class);
    }

    public EnumFacingMap(EnumMap<EnumFacing, ? extends V> m) {
        super(m);
    }

    public EnumFacingMap(Map<EnumFacing, ? extends V> m) {
        super(m);
    }

    /**
     * Make a new empty map.
     * @param <V> Value type.
     * @return The new map.
     */
    public static <V> EnumFacingMap<V> newMap() {
        return new EnumFacingMap<V>();
    }

    /**
     * Copy a map.
     * @param m The existing map.
     * @param <V> Value type.
     * @return The new map.
     */
    public static <V> EnumFacingMap<V> newMap(EnumMap<EnumFacing, ? extends V> m) {
        return new EnumFacingMap<V>(m);
    }

    /**
     * Copy a map.
     * @param m The existing map.
     * @param <V> Value type.
     * @return The new map.
     */
    public static <V> EnumFacingMap<V> newMap(Map<EnumFacing, ? extends V> m) {
        return new EnumFacingMap<V>(m);
    }

    /**
     * Make a new map for all the given facing values.
     * @param down Down
     * @param up Up
     * @param north North
     * @param south South
     * @param west West
     * @param east East
     * @param <V> Value type.
     * @return The new map.
     */
    public static <V> EnumFacingMap<V> forAllValues(V down, V up, V north, V south, V west, V east) {
        EnumFacingMap<V> map = new EnumFacingMap<V>();
        map.put(EnumFacing.DOWN, down);
        map.put(EnumFacing.UP, up);
        map.put(EnumFacing.NORTH, north);
        map.put(EnumFacing.SOUTH, south);
        map.put(EnumFacing.WEST, west);
        map.put(EnumFacing.EAST, east);
        return map;
    }
}
