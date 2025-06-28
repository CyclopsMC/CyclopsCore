package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * Separate elements that can be appended to sections.
 *
 * @author rubensworks
 */
public abstract class SectionAppendix {

    private final IInfoBook infoBook;
    private int page;
    private int lineStart;

    public SectionAppendix(IInfoBook infoBook) {
        this.infoBook = infoBook;
    }

    /**
     * @return The full height of this element with offsets.
     */
    public int getFullHeight() {
        return getHeight() + getOffsetY() * 2;
    }

    protected abstract int getOffsetY();

    protected abstract int getWidth();

    protected abstract int getHeight();

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
    @OnlyIn(Dist.CLIENT)
    public void drawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my, boolean pre) {
        int xc = x + width / 2 - getWidth() / 2;
        int yc = y + getOffsetY();
        if (pre) {
            drawElement(gui, guiGraphics, xc, yc, getWidth(), getHeight(), page, mx, my);
        } else {
            postDrawElement(gui, guiGraphics, xc, yc, getWidth(), getHeight(), page, mx, my);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    @OnlyIn(Dist.CLIENT)
    protected abstract void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    public abstract void preBakeElement(InfoSection infoSection);

    /**
     * Bake this appendix, only called once before changing pages.
     *
     * @param infoSection The section this appendix is part of.w
     */
    public abstract void bakeElement(InfoSection infoSection);

    public IInfoBook getInfoBook() {
        return this.infoBook;
    }

    public int getPage() {
        return this.page;
    }

    public int getLineStart() {
        return this.lineStart;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLineStart(int lineStart) {
        this.lineStart = lineStart;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SectionAppendix)) return false;
        final SectionAppendix other = (SectionAppendix) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$infoBook = this.getInfoBook();
        final Object other$infoBook = other.getInfoBook();
        if (this$infoBook == null ? other$infoBook != null : !this$infoBook.equals(other$infoBook)) return false;
        if (this.getPage() != other.getPage()) return false;
        if (this.getLineStart() != other.getLineStart()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SectionAppendix;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $infoBook = this.getInfoBook();
        result = result * PRIME + ($infoBook == null ? 43 : $infoBook.hashCode());
        result = result * PRIME + this.getPage();
        result = result * PRIME + this.getLineStart();
        return result;
    }

    public String toString() {
        return "SectionAppendix(infoBook=" + this.getInfoBook() + ", page=" + this.getPage() + ", lineStart=" + this.getLineStart() + ")";
    }
}
