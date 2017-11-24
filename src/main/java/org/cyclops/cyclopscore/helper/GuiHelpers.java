package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * Helpers related to guis.
 */
public class GuiHelpers {

    /**
     * Render a fluid tank in a gui.
     *
     * @param gui The gui to render in.
     * @param fluidStack The fluid to render.
     * @param capacity The tank capacity.
     * @param x The gui x position, including gui left.
     * @param y The gui y position, including gui top.
     * @param width The tank width.
     * @param height The tank height.
     */
    public static void renderFluidTank(Gui gui, @Nullable FluidStack fluidStack, int capacity,
                                       int x, int y, int width, int height) {
        if (fluidStack != null && capacity > 0) {
            int level = height * fluidStack.amount / capacity;
            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluidStack, EnumFacing.UP);
            int verticalOffset = 0;
            while(level > 0) {
                int textureHeight;
                if(level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }

                RenderHelpers.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                gui.drawTexturedModalRect(x, y - textureHeight - verticalOffset + height, icon, width, textureHeight);
                verticalOffset = verticalOffset + 16;
            }
        }
    }

    /**
     * Render a fluid tank in a gui with a tank overlay.
     * This assumes that the tank overlay has the provided width and height.
     *
     * @param gui The gui to render in.
     * @param fluidStack The fluid to render.
     * @param capacity The tank capacity.
     * @param x The gui x position, including gui left.
     * @param y The gui y position, including gui top.
     * @param width The tank width.
     * @param height The tank height.
     * @param textureOverlay The texture of the tank overlay.
     * @param overlayTextureX The overlay x texture position.
     * @param overlayTextureY The overlay y texture position.
     */
    public static void renderOverlayedFluidTank(Gui gui, @Nullable FluidStack fluidStack, int capacity,
                                                int x, int y, int width, int height,
                                                ResourceLocation textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            GlStateManager.enableBlend();
            RenderHelpers.bindTexture(textureOverlay);
            gui.drawTexturedModalRect(x, y, overlayTextureX, overlayTextureY, width, height);
        }
    }

}
