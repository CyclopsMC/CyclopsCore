package org.cyclops.cyclopscore.infobook.pageelement;

import lombok.Data;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.lwjgl.opengl.GL11;

/**
 * Separate elements that can be appended to sections.
 * @author rubensworks
 */
@Data public abstract class SectionAppendix {

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
     * @param gui The gui.
     * @param x Start X.
     * @param y Start Y.
     * @param width Max width.
     * @param height Max height.
     * @param page Current page.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param pre If the normal drawing should occur, otherwise post-drawing: things like tooltips.
     */
    @SideOnly(Side.CLIENT)
    public void drawScreen(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my, boolean pre) {
        int xc = x + width / 2 - getWidth() / 2;
        int yc = y + getOffsetY();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);
        if(pre) {
            drawElement(gui, xc, yc, getWidth(), getHeight(), page, mx, my);
        } else {
            postDrawElement(gui, xc, yc, getWidth(), getHeight(), page, mx, my);
        }
        GlStateManager.disableBlend();
    }

    @SideOnly(Side.CLIENT)
    protected abstract void drawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my);
    @SideOnly(Side.CLIENT)
    protected abstract void postDrawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my);

    public abstract void preBakeElement(InfoSection infoSection);
    /**
     * Bake this appendix, only called once before changing pages.
     * @param infoSection The section this appendix is part of.
     */
    public abstract void bakeElement(InfoSection infoSection);

}
