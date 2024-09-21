package org.cyclops.cyclopscore.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.cyclops.cyclopscore.helper.IModHelpers;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * A reusable scrollbar for screens.
 *
 * The using screen must add this as a child
 * and call the following method from its respective method:
 * * {@link #renderWidget(GuiGraphics, int, int, float)}
 * * {@link #mouseDragged(double, double, int, double, double)} (@see ContainerScreenScrolling for an example)
 *
 * @author rubensworks
 */
public class WidgetScrollBar extends AbstractWidget {

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("container/creative_inventory/scroller");
    private static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/creative_inventory/scroller_disabled");
    private static final int SCROLL_BUTTON_HEIGHT = 15;
    private static final int SCROLL_BUTTON_WIDTH = 12;

    private final int x;
    private final int y;
    private final int height;
    @Nullable
    private final IScrollCallback scrollCallback;
    @Nullable
    private final Rectangle scollRegion;

    private int totalRows;
    private int visibleRows;
    private float currentScroll; // (0 = top, 1 = bottom)
    private boolean isScrolling; // if the scrollbar is being dragged
    private boolean wasClicking; // if the left mouse button was held down last time drawScreen was called

    public WidgetScrollBar(int x, int y, int height, Component narrationMessage,
                           @Nullable IScrollCallback scrollCallback, int visibleRows) {
        this(x, y, height, narrationMessage, scrollCallback, visibleRows, null);
    }

    public WidgetScrollBar(int x, int y, int height, Component narrationMessage,
                           @Nullable IScrollCallback scrollCallback, int visibleRows, Rectangle scollRegion) {
        super(x, y, WidgetScrollBar.SCROLL_BUTTON_WIDTH, height, narrationMessage);
        this.x = x;
        this.y = y;
        this.height = height;
        this.scrollCallback = scrollCallback;
        this.scollRegion = scollRegion;

        this.currentScroll = 0;
        this.isScrolling = false;
        this.wasClicking = false;
        setVisibleRows(visibleRows);
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        if (scollRegion != null) {
            if (IModHelpers.get().getRenderHelpers().isPointInRegion(scollRegion, new Point((int) x, (int) y))) {
                return true;
            }
        }
        return super.isMouseOver(x, y);
    }

    /**
     * @return The current scroll position.
     */
    public float getCurrentScroll() {
        return currentScroll;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0 && this.needsScrollBars()) {
            scrollRelative(scrollY);
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
        if (!needsScrollBars()) {
            scrollTo(0);
            return true;
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
            this.currentScroll = Mth.clamp(this.currentScroll, 0.0F, 1.0F);
            scrollTo(this.currentScroll);
            return true;
        }

        return false;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int scrollX = x;
        int scrollMinY = y;
        int scrollMaxY = scrollMinY + height;
        guiGraphics.blitSprite(
                this.needsScrollBars() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE,
                scrollX,
                scrollMinY + (int)((float)(scrollMaxY - scrollMinY - SCROLL_BUTTON_HEIGHT - 2) * this.currentScroll),
                SCROLL_BUTTON_WIDTH,
                SCROLL_BUTTON_HEIGHT
        );
    }

    protected boolean needsScrollBars() {
        return getTotalRows() > getVisibleRows();
    }

    protected int getScrollStep() {
        return getTotalRows() - getVisibleRows();
    }

    public void scrollRelative(double step) {
        float scroll = (float)(this.currentScroll - step / getScrollStep());
        scroll = Mth.clamp(scroll, 0.0F, 1.0F);
        scrollTo(scroll);
    }

    public void scrollTo(float scroll) {
        scrollTo(scroll, true);
    }

    public void scrollTo(float scroll, boolean invokeCallback) {
        this.currentScroll = Math.max(0, scroll);
        if (invokeCallback && scrollCallback != null) {
            int firstRow = (int) ((double) (scroll * getScrollStep()) + 0.5D);
            scrollCallback.onScroll(firstRow);
        }
    }

    public void setFirstRow(int firstRow, boolean invokeCallback) {
        float scroll = ((float) firstRow) / getScrollStep();
        scroll = Mth.clamp(scroll, 0.0F, 1.0F);
        scrollTo(scroll, invokeCallback);
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

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public static interface IScrollCallback {

        public void onScroll(int firstRow);

    }
}
