package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public interface IGuiHelpers {

    /**
     * @return The default item slot size. Width and height are equal.
     */
    public int getSlotSize();

    /**
     * @return The default inner item slot size. Width and height are equal.
     */
    public int getSlotSizeInner();

    /**
     * Render a progress bar in a certain direction.
     * The currently bound texture will be used to render the progress bar.
     *
     * @param gui The gui to render in.
     * @param texture The texture.
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
    public void renderProgressBar(GuiGraphics gui, ResourceLocation texture, int x, int y, int width, int height, int textureX, int textureY,
                                  ProgressDirection direction, int progress, int progressMax);

    /**
     * Draw a tooltip.
     * @param gui The gui to draw in.
     * @param poseStack The pose stack.
     * @param lines A list of lines.
     * @param x Tooltip X.
     * @param y Tooltip Y.
     */
    public void drawTooltip(AbstractContainerScreen gui, PoseStack poseStack, List<Component> lines, int x, int y);

    /**
     * Draw the background box for a tooltip.
     * @param poseStack The pose stack.
     * @param xStart X
     * @param yStart Y
     * @param tooltipWidth Width
     * @param tooltipHeight Height
     */
    public void drawTooltipBackground(PoseStack poseStack, int xStart, int yStart, int tooltipWidth, int tooltipHeight);

    /**
     * Render a rectangle.
     * Public variant of GuiGraphics#fillGradient.
     *
     * @param poseStack The pose stack.
     * @param left Left X.
     * @param top Top Y.
     * @param right Right X.
     * @param bottom Bottom Y.
     * @param startColor Start gradient color.
     * @param endColor End gradient color.
     * @param zLevel The Z level to render at.
     */
    public void fillGradient(PoseStack poseStack, int left, int top, int right, int bottom, int startColor, int endColor, float zLevel);

    /**
     * Render a tooltip if the mouse if in the bounding box defined by the given position, width and height.
     * The tooltip lines supplier can return an optional list.
     * @param poseStack The pose stack.
     * @param gui The gui to render in.
     * @param x The gui x position, excluding gui left.
     * @param y The gui y position, excluding gui top.
     * @param width The area width.
     * @param height The area height.
     * @param mouseX The mouse x position.
     * @param mouseY The mouse y position.
     * @param linesSupplier A supplier for the optional tooltip lines to render.
     *                      No tooltip will be rendered when the optional value is absent.
     *                      This will only be called when needed.
     */
    public void renderTooltipOptional(AbstractContainerScreen gui, PoseStack poseStack, int x, int y, int width, int height,
                                      int mouseX, int mouseY, Supplier<Optional<List<Component>>> linesSupplier);

    /**
     * Render a tooltip if the mouse if in the bounding box defined by the given position, width and height.
     * @param gui The gui to render in.
     * @param poseStack The pose stack.
     * @param x The gui x position, excluding gui left.
     * @param y The gui y position, excluding gui top.
     * @param width The area width.
     * @param height The area height.
     * @param mouseX The mouse x position.
     * @param mouseY The mouse y position.
     * @param linesSupplier A supplier for the tooltip lines to render.
     *                      This will only be called when needed.
     */
    public void renderTooltip(AbstractContainerScreen gui, PoseStack poseStack, int x, int y, int width, int height,
                              int mouseX, int mouseY, Supplier<List<Component>> linesSupplier);

    /**
     * Stringify a (potentially large) quantity to a scaled string.
     * For example, 123765 will be converted as 1.23M.
     *
     * @param quantity A quantity.
     * @return A scaled quantity string.
     */
    public String quantityToScaledString(long quantity);

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
