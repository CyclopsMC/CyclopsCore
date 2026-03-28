package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public abstract class SectionAppendixClient<A extends SectionAppendix<?>> {

    private final A sectionAppendix;

    protected SectionAppendixClient(A sectionAppendix) {
        this.sectionAppendix = sectionAppendix;
    }

    public A getSectionAppendix() {
        return sectionAppendix;
    }

    /**
     * Draw the appendix.
     *
     * @param gui         The gui.
     * @param guiGraphics The gui graphics object.
     * @param x           Start X.
     * @param y           Start Y.
     * @param width       Max width.
     * @param height      Max height.
     * @param page        Current page.
     * @param mx          Mouse X.
     * @param my          Mouse Y.
     * @param pre         If the normal drawing should occur, otherwise post-drawing: things like tooltips.
     */
    public void drawScreen(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my, boolean pre) {
        int xc = x + width / 2 - getSectionAppendix().getWidth() / 2;
        int yc = y + getSectionAppendix().getOffsetY();
        if (pre) {
            drawElement(gui, guiGraphics, xc, yc, getSectionAppendix().getWidth(), getSectionAppendix().getHeight(), page, mx, my);
        } else {
            postDrawElement(gui, guiGraphics, xc, yc, getSectionAppendix().getWidth(), getSectionAppendix().getHeight(), page, mx, my);
        }
    }

    protected abstract void drawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    protected abstract void postDrawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

}
