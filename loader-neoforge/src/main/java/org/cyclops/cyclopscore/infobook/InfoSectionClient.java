package org.cyclops.cyclopscore.infobook;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;

import java.util.List;

/**
 * @author rubensworks
 */
public class InfoSectionClient {

    private final InfoSection infoSection;

    public InfoSectionClient(InfoSection infoSection) {
        this.infoSection = infoSection;
    }

    /**
     * Draw the screen for a given page.
     *
     * @param gui             The gui.
     * @param guiGraphics     The gui graphics object
     * @param mouseX          X.
     * @param mouseY          Y.
     * @param yOffset         The y offset.
     * @param width           The width of the page.
     * @param height          The height of the page.
     * @param page            The page to render.
     * @param mx              Mouse X.
     * @param my              Mouse Y.
     * @param footnoteOffsetX Footnote offset x
     * @param footnoteOffsetY Footnote offset y
     */
    public void drawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int mouseX, int mouseY, int yOffset, int width, int height, int page, int mx, int my, int footnoteOffsetX, int footnoteOffsetY) {
        if (page < this.infoSection.getPages()) {
            Font fontRenderer = gui.getFont();

            // Draw text content
            List<FormattedCharSequence> lines = this.infoSection.getLocalizedPageLines(page);
            int l = 0;
            if (lines != null) {
                for (FormattedCharSequence line : lines) {
                    guiGraphics.drawString(fontRenderer, line, mouseX, mouseY + yOffset + l * 9, 0, false);
                    l++;
                }
            }

            // Draw title if on first page
            if (this.infoSection.isTitlePage(page)) {
                gui.drawScaledCenteredString(guiGraphics, this.infoSection.getLocalizedTitle(), mouseX, mouseY + yOffset + 10, width, 1.5f, width, gui.getTitleColor());
                gui.drawHorizontalRule(guiGraphics, mouseX + width / 2, mouseY + yOffset);
                gui.drawHorizontalRule(guiGraphics, mouseX + width / 2, mouseY + yOffset + 21);
            }

            // Draw current page/section indication
            gui.drawScaledCenteredString(guiGraphics, this.infoSection.getLocalizedTitle() + " - " + (page + 1) + "/" + this.infoSection.getPages(), mouseX + (((page % 2 == 0) ? 1 : -1) * footnoteOffsetX), mouseY + height + footnoteOffsetY, width, 0.6f, (int) (width * 0.75f), IModHelpers.get().getBaseHelpers().RGBAToInt(190, 190, 190, 255));

            // Draw appendixes
            for (SectionAppendix appendix : this.infoSection.getAppendixes()) {
                if (appendix.getPage() == page) {
                    appendix.getSectionAppendixClient().drawScreen(gui, guiGraphics, mouseX, mouseY + yOffset + this.infoSection.getAppendixOffsetLine(fontRenderer, appendix),
                            width, height, page, mx, my, true);
                }
            }
        }
    }

    /**
     * Draw the overlays for the given page, for tooltips and such.
     *
     * @param gui         The gui.
     * @param guiGraphics The gui graphics object.
     * @param mouseX      X.
     * @param mouseY      Y.
     * @param width       The width of the page.
     * @param height      The height of the page.
     * @param page        The page to render.
     * @param mx          Mouse X.
     * @param my          Mouse Y.
     */
    public void postDrawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height, int page, int mx, int my) {
        if (page < this.infoSection.getPages()) {
            Font fontRenderer = gui.getFont();
            // Post draw appendixes
            for (SectionAppendix appendix : this.infoSection.getAppendixes()) {
                if (appendix.getPage() == page) {
                    appendix.getSectionAppendixClient().drawScreen(gui, guiGraphics, mouseX, mouseY + this.infoSection.getAppendixOffsetLine(fontRenderer, appendix),
                            width, height, page, mx, my, false);
                }
            }
        }
    }

}
