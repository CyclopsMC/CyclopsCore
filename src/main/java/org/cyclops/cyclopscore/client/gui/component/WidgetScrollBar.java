package org.cyclops.cyclopscore.client.gui.component;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.cyclopscore.helper.RenderHelpers;

import javax.annotation.Nullable;

/**
 * A reusable scrollbar for screens.
 *
 * The using screen must add this as a child
 * and call the following method from its respective method:
 * * {@link #drawGuiContainerBackgroundLayer(float, int, int)}
 *
 * @author rubensworks
 */
public class WidgetScrollBar extends Widget {

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

    public WidgetScrollBar(int x, int y, int height, String narrationMessage,
                           @Nullable IScrollCallback scrollCallback, int visibleRows) {
        super(x, y, WidgetScrollBar.SCROLL_BUTTON_WIDTH, height, narrationMessage);
        this.x = x;
        this.y = y;
        this.height = height;
        this.scrollCallback = scrollCallback;

        this.currentScroll = 0;
        this.isScrolling = false;
        this.wasClicking = false;
        setVisibleRows(visibleRows);
    }

    /**
     * @return The current scroll position.
     */
    public float getCurrentScroll() {
        return currentScroll;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (scroll != 0 && this.needsScrollBars()) {
            scrollRelative(scroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double offsetX, double offsetY) {
        boolean flag = mouseButton == 0 || mouseButton == 1;
        int xMax = x + 14;
        int yMax = y + height;

        // Reset scroll if too big for current view
        if (!needsScrollBars() && currentScroll > 0) {
            scrollTo(0);
        }

        if (!this.wasClicking && flag && mouseX >= x && mouseY >= y && mouseX < xMax && mouseY < yMax) {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag) {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling) {
            this.currentScroll = ((float)(mouseY - y) - 7.5F) / ((float)(yMax - y) - 15.0F);
            this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
            scrollTo(this.currentScroll);
            return true;
        }

        return false;
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int scrollX = x;
        int scrollMinY = y;
        int scrollMaxY = scrollMinY + height;
        RenderHelpers.bindTexture(SCROLLBUTTON);
        this.blit(
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

    protected void scrollRelative(double step) {
        float scroll = (float)(this.currentScroll - step / getScrollStep());
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
