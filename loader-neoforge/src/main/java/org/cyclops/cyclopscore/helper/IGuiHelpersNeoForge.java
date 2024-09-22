package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public interface IGuiHelpersNeoForge extends IGuiHelpers {

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
    public void renderFluidTank(GuiGraphics gui, @Nullable FluidStack fluidStack, int capacity,
                                int x, int y, int width, int height);

    /**
     * Render the given fluid in a standard slot.
     * @param gui The gui to render in.
     * @param fluidStack The fluid to render.
     * @param x The slot X position.
     * @param y The slot Y position.
     */
    public void renderFluidSlot(GuiGraphics gui, @Nullable FluidStack fluidStack, int x, int y);

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
    public void renderOverlayedFluidTank(GuiGraphics gui, @Nullable FluidStack fluidStack, int capacity,
                                         int x, int y, int width, int height,
                                         ResourceLocation textureOverlay, int overlayTextureX, int overlayTextureY);

}
