package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.HashMap;
import java.util.Locale;
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

    private static final Map<String, String> MODRESOURCEDOMAIN_TO_MODID = Maps.newHashMap();

    /**
     * Get the mod id from the given resource domain the given mod is using.
     * @param modResourceDomain The mod resource domain.
     * @return The mod id.
     */
    public static String getModId(String modResourceDomain) {
        if(MODRESOURCEDOMAIN_TO_MODID.isEmpty()) {
            for (Map.Entry<String, ModContainer> entry : Loader.instance().getIndexedModList().entrySet()) {
                MODRESOURCEDOMAIN_TO_MODID.put(entry.getKey().toLowerCase(Locale.ENGLISH), entry.getValue().getModId());
            }
        }
        return MODRESOURCEDOMAIN_TO_MODID.get(modResourceDomain);
    }

    /**
     * @return If minecraft is past the POST-init phase.
     */
    public static boolean isMinecraftInitialized() {
        return Loader.instance().getLoaderState().ordinal() > LoaderState.POSTINITIALIZATION.ordinal();
    }

    /**
     * Safely get a capability from a tile or block.
     * The capability of the tile will be checked first,
     * only if it was not found, the block will be checked.
     * @param world The world.
     * @param pos The position of the tile or block providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The capability or null.
     */
    @Optional.Method(modid = "commoncapabilities")
    public static <C> C getTileOrBlockCapability(IBlockAccess world, BlockPos pos, EnumFacing side,
                                                 Capability<C> capability) {
        C instance = TileHelpers.getCapability(world, pos, side, capability);
        if (instance == null) {
            return BlockHelpers.getCapability(world, pos, side, capability);
        }
        return instance;
    }

}
