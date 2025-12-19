package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class GuiHelpersCommon implements IGuiHelpers {

    protected final IModHelpers modHelpers;

    public GuiHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public int getSlotSize() {
        return 18;
    }

    @Override
    public int getSlotSizeInner() {
        return 16;
    }

    @Override
    public void renderProgressBar(GuiGraphics gui, Identifier texture, int x, int y, int width, int height, int textureX, int textureY,
                                  ProgressDirection direction, int progress, int progressMax) {
        if (progressMax > 0 && progress > 0) {
            int scaledWidth = width;
            int scaledHeight = height;

            // Scale the width and/or height
            if (direction.getIncrementX() != 0) {
                scaledWidth = (int) (scaledWidth * (((double) progress) / progressMax));
            }
            if (direction.getIncrementY() != 0) {
                scaledHeight = (int) (scaledHeight * (((double) progress) / progressMax));
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

            gui.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, textureX, textureY, scaledWidth, scaledHeight, 256, 256);
        }
    }

    @Override
    public void drawTooltip(AbstractContainerScreen gui, GuiGraphics guiGraphics, List<Component> lines, int x, int y) {
        guiGraphics.setComponentTooltipForNextFrame(gui.getFont(), lines, x, y);
    }

    @Override
    public void renderTooltipOptional(AbstractContainerScreen gui, GuiGraphics guiGraphics, int x, int y, int width, int height,
                                             int mouseX, int mouseY, Supplier<Optional<List<Component>>> linesSupplier) {
        if (modHelpers.getRenderHelpers().isPointInRegion(x, y, width, height, mouseX - gui.leftPos, mouseY - gui.topPos)) {
            linesSupplier.get().ifPresent(
                    lines -> guiGraphics.setComponentTooltipForNextFrame(gui.getFont(), lines, mouseX, mouseY));
        }
    }

    @Override
    public void renderTooltip(AbstractContainerScreen gui, GuiGraphics guiGraphics, int x, int y, int width, int height,
                                     int mouseX, int mouseY, Supplier<List<Component>> linesSupplier) {
        renderTooltipOptional(gui, guiGraphics, x, y, width, height, mouseX, mouseY, () -> Optional.of(linesSupplier.get()));
    }

    private static final List<Pair<Long, String>> COUNT_SCALES = Lists.newArrayList(
            Pair.of(1000000000000000000L, "E"),
            Pair.of(1000000000000000L, "P"),
            Pair.of(1000000000000L, "T"),
            Pair.of(1000000000L, "G"),
            Pair.of(1000000L, "M"),
            Pair.of(1000L, "K")
    );

    @Override
    public String quantityToScaledString(long quantity) {
        for (Pair<Long, String> countScale : COUNT_SCALES) {
            long scale = countScale.getLeft();
            if (quantity >= scale) {
                long division = quantity / scale;
                String divisionString = String.valueOf(division);

                // Add digits if string is short
                if (division < 10) {
                    long mod = quantity % scale;
                    if (mod > 0) {
                        long digits = mod * 100 / scale;
                        divisionString += "." + (digits < 10 ? "0" : "") + String.valueOf(digits);
                    }
                } else if (division < 100) {
                    long mod = quantity % scale;
                    if (mod > 0) {
                        long digits = mod * 10 / scale;
                        divisionString += "." + String.valueOf(digits);
                    }
                }

                return divisionString + countScale.getRight();
            }
        }
        return String.valueOf(quantity);
    }
}
