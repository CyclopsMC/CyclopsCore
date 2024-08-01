package org.cyclops.cyclopscore.datastructure;

import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.Map;

/**
 * An efficient implementation for mapping facings to any value.
 * @author rubensworks
 * @param <V> The value type.
 */
public class EnumFacingMap<V> extends EnumMap<Direction, V> {
    public EnumFacingMap() {
        super(Direction.class);
    }

    public EnumFacingMap(EnumMap<Direction, ? extends V> m) {
        super(m);
    }

    public EnumFacingMap(Map<Direction, ? extends V> m) {
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
    public static <V> EnumFacingMap<V> newMap(EnumMap<Direction, ? extends V> m) {
        return new EnumFacingMap<V>(m);
    }

    /**
     * Copy a map.
     * @param m The existing map.
     * @param <V> Value type.
     * @return The new map.
     */
    public static <V> EnumFacingMap<V> newMap(Map<Direction, ? extends V> m) {
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
        map.put(Direction.DOWN, down);
        map.put(Direction.UP, up);
        map.put(Direction.NORTH, north);
        map.put(Direction.SOUTH, south);
        map.put(Direction.WEST, west);
        map.put(Direction.EAST, east);
        return map;
    }
}
