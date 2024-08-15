package org.cyclops.cyclopscore.helper;

import org.apache.commons.lang3.tuple.Triple;

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
        return IModHelpers.get().getBaseHelpers().tryParse(newValue, oldValue);
    }

    /**
     * Convert r, g and b colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @return integer representation of the color.
     */
    public static int RGBToInt(int r, int g, int b) {
        return IModHelpers.get().getBaseHelpers().RGBToInt(r, g, b);
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
        return IModHelpers.get().getBaseHelpers().RGBAToInt(r, g, b, a);
    }

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-255
     * @return The color with alpha.
     */
    public static int addAlphaToColor(int color, int alpha) {
        return IModHelpers.get().getBaseHelpers().addAlphaToColor(color, alpha);
    }

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-1
     * @return The color with alpha.
     */
    public static int addAlphaToColor(int color, float alpha) {
        return IModHelpers.get().getBaseHelpers().addAlphaToColor(color, alpha);
    }

    /**
     * Convert a color in integer representation to seperated r, g and b colors.
     * @param color The color in integer representation.
     * @return The separated r, g and b colors.
     */
    public static Triple<Float, Float, Float> intToRGB(int color) {
        return IModHelpers.get().getBaseHelpers().intToRGB(color);
    }

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @param alpha The alpha to apply
     * @return The color in BGR
     */
    public static int rgbToBgra(int color, int alpha) {
        return IModHelpers.get().getBaseHelpers().rgbToBgra(color, alpha);
    }

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @return The color in BGR
     */
    public static int rgbToBgr(int color) {
        return IModHelpers.get().getBaseHelpers().rgbToBgr(color);
    }

    /**
     * Take the sum of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Integer
     * @param b Integer
     * @return The safe sum.
     */
    public static int addSafe(int a, int b) {
        return IModHelpers.get().getBaseHelpers().addSafe(a, b);
    }

    /**
     * Take the multiplication of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Positive Integer
     * @param b Positive Integer
     * @return The safe multiplication.
     */
    public static int multiplySafe(int a, int b) {
        return IModHelpers.get().getBaseHelpers().multiplySafe(a, b);
    }

    /**
     * Cast a long value safely to an int.
     * If the casting would result in an overflow,
     * return the {@link Integer#MAX_VALUE}.
     * @param value A value to cast.
     * @return The casted value.
     */
    public static int castSafe(long value) {
        return IModHelpers.get().getBaseHelpers().castSafe(value);
    }

    /**
     * Open the given URL in the player's browser.
     * @param url An URL.
     */
    public static void openUrl(String url) {
        IModHelpers.get().getBaseHelpers().openUrl(url);
    }

}
