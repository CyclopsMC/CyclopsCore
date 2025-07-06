package org.cyclops.cyclopscore.infobook.pageelement;

import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

/**
 * Separate elements that can be appended to sections.
 *
 * @author rubensworks
 */
public abstract class SectionAppendix<C extends SectionAppendixClient<?>> {

    private final IInfoBook infoBook;
    private int page;
    private int lineStart;
    private C sectionAppendixClient;

    public SectionAppendix(IInfoBook infoBook) throws InfoBookParser.InvalidAppendixException {
        this.infoBook = infoBook;
        if (IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            this.sectionAppendixClient = constructSectionAppendixClient();
        }
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

    public abstract C constructSectionAppendixClient() throws InfoBookParser.InvalidAppendixException;

    public C getSectionAppendixClient() {
        return sectionAppendixClient;
    }

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
