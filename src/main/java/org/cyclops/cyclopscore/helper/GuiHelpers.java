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

    /**
     * Render a progress bar in a certain direction.
     * The currently bound texture will be used to render the progress bar.
     *
     * @param gui The gui to render in.
     * @param x The gui x position, including gui left.
     * @param y The gui y position, including gui top.
     * @param width The progress bar width.
     * @param height The progress bar height.
     * @param textureX The texture x position.
     * @param textureY The texture y position.
     * @param direction The direction to progress in.
     * @param progress The current progress.
     * @param progressMax The maximum progress.
     */
    public static void renderProgressBar(Gui gui, int x, int y, int width, int height, int textureX, int textureY,
                                         ProgressDirection direction, int progress, int progressMax) {
        if (progressMax > 0 && progress > 0) {
            int scaledWidth = width;
            int scaledHeight = height;

            // Scale the width and/or height
            if (direction.getIncrementX() != 0) {
                scaledWidth = scaledWidth * progress / progressMax;
            }
            if (direction.getIncrementY() != 0) {
                scaledHeight = scaledHeight * progress / progressMax;
            }

            // If increments happen inversely, make sure we start incrementing from the other end of the progress bar
            if (direction.getIncrementX() < 0) {
                int offset = width - scaledWidth;
                x += offset;
                textureX += offset;
            }
            if (direction.getIncrementY() < 0) {
                int offset = height - scaledHeight;
                y += offset;
                textureY += offset;
            }

            gui.drawTexturedModalRect(x, y, textureX, textureY, scaledWidth, scaledHeight);
        }
    }

    /**
     * Represents the direction of a progress bar.
     */
    public static enum ProgressDirection {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0),

        UP_LEFT(-1, -1),
        UP_RIGHT(1, -1),
        DOWN_LEFT(-1, 1),
        DOWN_RIGHT(1, 1);

        private final int incrementX;
        private final int incrementY;

        private ProgressDirection(int incrementX, int incrementY) {
            this.incrementX = incrementX;
            this.incrementY = incrementY;
        }

        public int getIncrementX() {
            return incrementX;
        }

        public int getIncrementY() {
            return incrementY;
        }
    }

}
