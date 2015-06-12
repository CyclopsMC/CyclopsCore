package org.cyclops.cyclopscore.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of helper methods and fields.
 * @author rubensworks
 *
 */
public class Helpers {
    
    private static Map<Pair<ModBase, IDType>, Integer> ID_COUNTER = new HashMap<Pair<ModBase, IDType>, Integer>();
    
    /**
     * Safe parsing of a string to it's real object type.
     * The real object type is determined by checking the class of the oldValue.
     * @param newValue The value to parse
     * @param oldValue The old value that has a certain type.
     * @return The parsed newValue.
     */
    public static Object tryParse(String newValue, Object oldValue) {
        Object newValueParsed = null;
        try {
            if(oldValue instanceof Integer) {
                newValueParsed = Integer.parseInt(newValue);
            } else if(oldValue instanceof Boolean) {
                newValueParsed = Boolean.parseBoolean(newValue);
            } else if(oldValue instanceof Double) {
                newValueParsed = Double.parseDouble(newValue);
            } else if(oldValue instanceof String) {
                newValueParsed = newValue;
            }
        } catch (Exception e) {}
        return newValueParsed;
    }
    
    /**
     * Get a new ID for the given type.
     * @param type Type for a {@link org.cyclops.cyclopscore.config.configurable.IConfigurable}.
     * @param mod The mod to register the id for.
     * @return The incremented ID.
     */
    public static int getNewId(ModBase mod, IDType type) {
    	Integer ID = ID_COUNTER.get(Pair.of(mod, type));
    	if(ID == null) ID = 0;
    	ID_COUNTER.put(Pair.of(mod, type), ID + 1);
    	return ID;
    }
    
    /**
     * Type of ID's to use in {@link Helpers#getNewId(ModBase, IDType)}
     * @author rubensworks
     *
     */
    public enum IDType {
    	/**
    	 * Entity ID.
    	 */
    	ENTITY,
    	/**
    	 * GUI ID.
    	 */
    	GUI,
    	/**
    	 * Packet ID.
    	 */
    	PACKET;
    }

    /**
     * Convert r, g and b colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @return integer representation of the color.
     */
    public static int RGBToInt(int r, int g, int b) {
        return (int)r << 16 | (int)g << 8 | (int)b;
    }

    /**
     * Convert a color in integer representation to seperated r, g and b colors.
     * @param color The color in integer representation.
     * @return The separated r, g and b colors.
     */
    public static Triple<Float, Float, Float> intToRGB(int color) {
        float red, green, blue;
        red = (float)(color >> 16 & 255) / 255.0F;
        green = (float)(color >> 8 & 255) / 255.0F;
        blue = (float)(color & 255) / 255.0F;
        //this.alpha = (float)(color >> 24 & 255) / 255.0F;
        return Triple.of(red, green, blue);
    }

}
