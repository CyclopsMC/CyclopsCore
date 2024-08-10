package org.cyclops.cyclopscore.helper;

import net.minecraft.Util;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;

import java.net.URI;

/**
 * A collection of helper methods and fields.
 * @author rubensworks
 *
 */
@Deprecated // TODO: remove in next major version
public class Helpers {

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
     * Take the multiplication of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Positive Integer
     * @param b Positive Integer
     * @return The safe multiplication.
     */
    public static int multiplySafe(int a, int b) {
        int mul = a * b;
        if(a > 0 && b > 0 && (mul < a || mul < b)) return Integer.MAX_VALUE;
        return mul;
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
     * Open the given URL in the player's browser.
     * @param url An URL.
     */
    public static void openUrl(String url) {
        try {
            URI uri = new URI(url);
            Util.getPlatform().openUri(uri);
        } catch (Throwable e) {
            e.printStackTrace();
            CyclopsCore.clog(Level.ERROR, e.getMessage());
        }
    }

}
