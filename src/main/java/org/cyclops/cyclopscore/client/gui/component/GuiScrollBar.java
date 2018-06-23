package org.cyclops.cyclopscore.client.gui.component;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;

/**
 * A reusable scrollbar for guis.
 *
 * The using gui must call the following methods from its respective method:
 * * {@link #handleMouseInput()}
 * * {@link #drawCurrentScreen(int, int, float)}
 * * {@link #drawGuiContainerBackgroundLayer(float, int, int)}
 *
 * @author rubensworks
 */
public class GuiScrollBar extends Gui {

    private static final ResourceLocation SCROLLBUTTON = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final int SCROLL_BUTTON_HEIGHT = 15;
    private static final int SCROLL_BUTTON_WIDTH = 12;

    private final int x;
    private final int y;
    private final int height;
    @Nullable
    private final IScrollCallback scrollCallback;

    private int totalRows;
    private int visibleRows;
    private float currentScroll; // (0 = top, 1 = bottom)
    private boolean isScrolling; // if the scrollbar is being dragged
    private boolean wasClicking; // if the left mouse button was held down last time drawScreen was called

    public GuiScrollBar(int x, int y, int height, @Nullable IScrollCallback scrollCallback, int visibleRows) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.scrollCallback = scrollCallback;

        this.currentScroll = 0;
        this.isScrolling = false;
        this.wasClicking = false;
        setVisibleRows(visibleRows);
    }

    public void handleMouseInput() {
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            if (i > 0) i = 1;
            if (i < 0) i = -1;

            scrollRelative(i);
        }
    }

    public void drawCurrentScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag = Mouse.isButtonDown(0);
        int i1 = x;
        int j1 = y;
        int k1 = i1 + 14;
        int l1 = j1 + height;

        if (!this.wasClicking && flag && mouseX >= i1 && mouseY >= j1 && mouseX < k1 && mouseY < l1) {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag) {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling) {
            this.currentScroll = ((float)(mouseY - j1) - 7.5F) / ((float)(l1 - j1) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            scrollTo(this.currentScroll);
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int scrollX = x;
        int scrollMinY = y;
        int scrollMaxY = scrollMinY + height;
        RenderHelpers.bindTexture(SCROLLBUTTON);
        this.drawTexturedModalRect(
                scrollX,
                scrollMinY + (int)((float)(scrollMaxY - scrollMinY - SCROLL_BUTTON_HEIGHT - 2) * this.currentScroll),
                232 + (this.needsScrollBars() ? 0 : SCROLL_BUTTON_WIDTH),
                0,
                SCROLL_BUTTON_WIDTH,
                SCROLL_BUTTON_HEIGHT
        );
    }

    protected boolean needsScrollBars() {
        return getTotalRows() > getVisibleRows();
    }

    protected int getScrollStep() {
        return getTotalRows() - getVisibleRows() + 1;
    }

    protected void scrollRelative(int step) {
        float scroll = (float)((double)this.currentScroll - (double)step / (double)getScrollStep());
        scroll = MathHelper.clamp(scroll, 0.0F, 1.0F);
        scrollTo(scroll);
    }

    public void scrollTo(float scroll) {
        this.currentScroll = scroll;
        if (scrollCallback != null) {
            int firstRow = (int) ((double) (scroll * getScrollStep()) + 0.5D);
            scrollCallback.onScroll(firstRow);
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getVisibleRows() {
        return visibleRows;
    }

    public void setVisibleRows(int visibleRows) {
        this.visibleRows = visibleRows;
    }

    public static interface IScrollCallback {

        public void onScroll(int firstRow);

    }
}
