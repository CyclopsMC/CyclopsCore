package org.cyclops.cyclopscore.helper;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;

import java.net.URI;
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
     * @param type Type for something.
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
    	 * Packet ID.
    	 */
    	PACKET
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
     * Convert r, g, b and a colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     * @return integer representation of the color.
     */
    public static int RGBAToInt(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-255
     * @return The color with alpha.
     */
    public static int addAlphaToColor(int color, int alpha) {
        return alpha << 24 | color;
    }

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-1
     * @return The color with alpha.
     */
    public static int addAlphaToColor(int color, float alpha) {
        return addAlphaToColor(color, Math.round(alpha * 255F));
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

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @param alpha The alpha to apply
     * @return The color in BGR
     */
    public static int rgbToBgra(int color, int alpha) {
        Triple<Float, Float, Float> triple = Helpers.intToRGB(color);
        // RGB to BGR
        return Helpers.RGBAToInt(
                (int) (float) (triple.getRight() * 255F), (int) (float) (triple.getMiddle() * 255F), (int) (float) (triple.getLeft() * 255F),
                alpha);
    }

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @return The color in BGR
     */
    public static int rgbToBgr(int color) {
        return rgbToBgra(color, 255);
    }

    /**
     * Take the sum of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Integer
     * @param b Integer
     * @return The safe sum.
     */
    public static int addSafe(int a, int b) {
        int sum = a + b;
        if(sum < a || sum < b) return Integer.MAX_VALUE;
        return sum;
    }

    /**
     * Cast a long value safely to an int.
     * If the casting would result in an overflow,
     * return the {@link Integer#MAX_VALUE}.
     * @param value A value to cast.
     * @return The casted value.
     */
    public static int castSafe(long value) {
        int casted = (int) value;
        if (casted != value) {
            return Integer.MAX_VALUE;
        }
        return casted;
    }

    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public static boolean isDevEnvironment() {
        return "mcp".equals(Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.NAMING.get()).orElse("mojang"));
    }

    /**
     * @return If minecraft has been fully loaded.
     */
    public static boolean isMinecraftInitialized() {
        return CyclopsCore._instance.isLoaded();
    }

    /**
     * Open the given URL in the player's browser.
     * @param url An URL.
     */
    public static void openUrl(String url) {
        try {
            URI uri = new URI(url);
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke(null);
            oclass.getMethod("browse", URI.class).invoke(object, uri);
        } catch (Throwable e) {
            e.printStackTrace();
            CyclopsCore.clog(Level.ERROR, e.getMessage());
        }
    }

}
