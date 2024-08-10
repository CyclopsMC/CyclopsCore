package org.cyclops.cyclopscore.helper;

import net.minecraft.Util;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;

import java.net.URI;

/**
 * @author rubensworks
 */
public class BaseHelpersCommon implements IBaseHelpers {
    @Override
    public Object tryParse(String newValue, Object oldValue) {
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

    @Override
    public int RGBToInt(int r, int g, int b) {
        return (int)r << 16 | (int)g << 8 | (int)b;
    }

    @Override
    public int RGBAToInt(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public int addAlphaToColor(int color, int alpha) {
        return alpha << 24 | color;
    }

    @Override
    public int addAlphaToColor(int color, float alpha) {
        return addAlphaToColor(color, Math.round(alpha * 255F));
    }

    @Override
    public Triple<Float, Float, Float> intToRGB(int color) {
        float red, green, blue;
        red = (float)(color >> 16 & 255) / 255.0F;
        green = (float)(color >> 8 & 255) / 255.0F;
        blue = (float)(color & 255) / 255.0F;
        //this.alpha = (float)(color >> 24 & 255) / 255.0F;
        return Triple.of(red, green, blue);
    }

    @Override
    public int rgbToBgra(int color, int alpha) {
        Triple<Float, Float, Float> triple = intToRGB(color);
        // RGB to BGR
        return RGBAToInt(
                (int) (float) (triple.getRight() * 255F), (int) (float) (triple.getMiddle() * 255F), (int) (float) (triple.getLeft() * 255F),
                alpha);
    }

    @Override
    public int rgbToBgr(int color) {
        return rgbToBgra(color, 255);
    }

    @Override
    public int addSafe(int a, int b) {
        int sum = a + b;
        if(sum < a || sum < b) return Integer.MAX_VALUE;
        return sum;
    }

    @Override
    public int multiplySafe(int a, int b) {
        int mul = a * b;
        if(a > 0 && b > 0 && (mul < a || mul < b)) return Integer.MAX_VALUE;
        return mul;
    }

    @Override
    public int castSafe(long value) {
        int casted = (int) value;
        if (casted != value) {
            return Integer.MAX_VALUE;
        }
        return casted;
    }

    @Override
    public void openUrl(String url) {
        try {
            URI uri = new URI(url);
            Util.getPlatform().openUri(uri);
        } catch (Throwable e) {
            e.printStackTrace();
            CyclopsCoreInstance.MOD.log(Level.ERROR, e.getMessage());
        }
    }
}
