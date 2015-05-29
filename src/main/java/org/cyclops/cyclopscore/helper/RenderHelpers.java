package org.cyclops.cyclopscore.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
public class RenderHelpers {
    
    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
    	Minecraft.getMinecraft().renderEngine.bindTexture(texture);
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

}
