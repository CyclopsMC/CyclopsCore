package org.cyclops.cyclopscore.infobook.pageelement;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Data;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
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
     * @param guiGraphics The gui graphics object.
     * @param x Start X.
     * @param y Start Y.
     * @param width Max width.
     * @param height Max height.
     * @param page Current page.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param pre If the normal drawing should occur, otherwise post-drawing: things like tooltips.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my, boolean pre) {
        int xc = x + width / 2 - getWidth() / 2;
        int yc = y + getOffsetY();
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if(pre) {
            drawElement(gui, guiGraphics, xc, yc, getWidth(), getHeight(), page, mx, my);
        } else {
            postDrawElement(gui, guiGraphics, xc, yc, getWidth(), getHeight(), page, mx, my);
        }
        GlStateManager._disableBlend();
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my);
    @OnlyIn(Dist.CLIENT)
    protected abstract void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    public abstract void preBakeElement(InfoSection infoSection);
    /**
     * Bake this appendix, only called once before changing pages.
     * @param infoSection The section this appendix is part of.w
     */
    public abstract void bakeElement(InfoSection infoSection);

}
