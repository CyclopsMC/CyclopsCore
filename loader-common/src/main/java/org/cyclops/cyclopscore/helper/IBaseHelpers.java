package org.cyclops.cyclopscore.helper;

import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public interface IBaseHelpers {

    /**
     * Safe parsing of a string to it's real object type.
     * The real object type is determined by checking the class of the oldValue.
     * @param newValue The value to parse
     * @param oldValue The old value that has a certain type.
     * @return The parsed newValue.
     */
    public Object tryParse(String newValue, Object oldValue);

    /**
     * Convert r, g and b colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @return integer representation of the color.
     */
    public int RGBToInt(int r, int g, int b);

    /**
     * Convert r, g, b and a colors to an integer representation.
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     * @return integer representation of the color.
     */
    public int RGBAToInt(int r, int g, int b, int a);

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-255
     * @return The color with alpha.
     */
    public int addAlphaToColor(int color, int alpha);

    /**
     * Add the given alpha value to the given RGB color.
     * @param color The color.
     * @param alpha The alpha from 0-1
     * @return The color with alpha.
     */
    public int addAlphaToColor(int color, float alpha);

    /**
     * Convert a color in integer representation to seperated r, g and b colors.
     * @param color The color in integer representation.
     * @return The separated r, g and b colors.
     */
    public Triple<Float, Float, Float> intToRGB(int color);

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @param alpha The alpha to apply
     * @return The color in BGR
     */
    public int rgbToBgra(int color, int alpha);

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @return The color in BGR
     */
    public int rgbToBgr(int color);

    /**
     * Take the sum of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Integer
     * @param b Integer
     * @return The safe sum.
     */
    public int addSafe(int a, int b);

    /**
     * Take the multiplication of these two values capped at {@link Integer#MAX_VALUE}.
     * @param a Positive Integer
     * @param b Positive Integer
     * @return The safe multiplication.
     */
    public int multiplySafe(int a, int b);

    /**
     * Cast a long value safely to an int.
     * If the casting would result in an overflow,
     * return the {@link Integer#MAX_VALUE}.
     * @param value A value to cast.
     * @return The casted value.
     */
    public int castSafe(long value);

    /**
     * Open the given URL in the player's browser.
     * @param url An URL.
     */
    public void openUrl(String url);

}
