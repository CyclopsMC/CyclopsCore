package org.cyclops.cyclopscore.client.gui.container;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.cyclopscore.client.gui.component.WidgetScrollBar;
import org.cyclops.cyclopscore.client.gui.component.input.WidgetTextFieldExtended;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;

import java.awt.*;

/**
 * Gui for an inventory container that has a scrollbar and search field.
 * @author rubensworks
 */
public abstract class ContainerScreenScrolling<T extends ScrollingInventoryContainer> extends ContainerScreenExtended<T> {

    private static final int SEARCH_WIDTH = 89;

    private WidgetTextFieldExtended searchField = null;
    private WidgetScrollBar scrollbar = null;

    public ContainerScreenScrolling(T container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        if(isSearchEnabled()) {
            int searchWidth = getSearchWidth();
            int searchX = getSearchX();
            int searchY = getSearchY();
            if(this.searchField == null) {
                this.searchField = new WidgetTextFieldExtended(this.font, this.leftPos + searchX, this.topPos + searchY, searchWidth, this.font.lineHeight, Component.translatable("gui.cyclopscore.search"));
                this.searchField.setMaxLength(64);
                this.searchField.setMaxLength(15);
                this.searchField.setBordered(false);
                this.searchField.setVisible(true);
                this.searchField.setTextColor(ARGB.opaque(16777215));
                this.searchField.setCanLoseFocus(true);
                this.searchField.setValue("");
                this.searchField.setWidth(searchWidth);
                this.searchField.setX(this.leftPos + (searchX + searchWidth) - this.searchField.getWidth());
            } else {
                this.searchField.setWidth(searchWidth);
                this.searchField.setX(this.leftPos + (searchX + searchWidth) - this.searchField.getWidth());
                this.searchField.setY(this.topPos + searchY);
            }
            this.addWidget(this.searchField);
        }

        // Initial element load.
        if (scrollbar == null) {
            getMenu().updateFilter("");
            this.scrollbar = new WidgetScrollBar(this.leftPos + getScrollX(), this.topPos + getScrollY(), getScrollHeight(),
                    Component.translatable("gui.cyclopscore.scrollbar"), getMenu(),
                    getMenu().getPageSize(), getScrollRegion());
            this.scrollbar.setTotalRows(getTotalRows());
        } else {
            this.scrollbar.setX(this.leftPos + getScrollX());
            this.scrollbar.setY(this.topPos + getScrollY());
            this.scrollbar.setScollRegion(getScrollRegion());
        }

        this.addWidget(this.scrollbar);
        getScrollbar().scrollTo(this.scrollbar.getCurrentScroll());
    }

    /**
     * @return A custom region in which scrolling should also be allowed next to the scrollbar itself.
     */
    protected Rectangle getScrollRegion() {
        return null;
    }

    @Override
    public boolean charTyped(CharacterEvent character) {
        if (isSearchEnabled() && this.searchField.isFocused()) {
            if (this.searchField.charTyped(character)) {
                this.updateSearch(searchField.getValue());
            }
            return true;
        } else {
            return super.charTyped(character);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent key) {
        if (isSearchEnabled() && this.searchField.isFocused() && key.key() != InputConstants.KEY_ESCAPE) {
            if (this.searchField.keyPressed(key)) {
                this.updateSearch(searchField.getValue());
            }
            return true;
        } else {
            return super.keyPressed(key);
        }
    }

    @Override
    protected void drawCurrentScreen(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (isSubsetRenderSlots()) {
            // Temporarily swap slot list, to avoid rendering all slots (which would include the hidden ones)
            NonNullList<Slot> oldSlots = this.container.slots;
            int startIndex = getMenu().getFirstElement();
            NonNullList<Slot> newSlots = NonNullList.create();
            newSlots.addAll(oldSlots.subList(startIndex, Math.min(oldSlots.size(), startIndex
                    + (getMenu().getPageSize() * getMenu().getColumns()))));
            newSlots.addAll(oldSlots.subList(getMenu().getUnfilteredItemCount(), oldSlots.size()));
            this.container.slots = newSlots;
            super.drawCurrentScreen(guiGraphics, mouseX, mouseY, partialTicks);
            this.container.slots = oldSlots;
        } else {
            super.drawCurrentScreen(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * @return If the optimization should be done for only rendering the visible slots. Default: false
     */
    protected boolean isSubsetRenderSlots() {
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        if(isSearchEnabled()) this.searchField.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        this.scrollbar.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (this.getFocused() != null && this.isDragging() && event.button() == 0
                && this.getFocused().mouseDragged(event, mouseX, mouseY)) {
            return true;
        }
        return super.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ, double scroll) {
        return (this.scrollbar.isMouseOverRegion(mouseX, mouseY) && this.scrollbar.mouseScrolled(mouseX, mouseY, mouseZ, scroll))
                || super.mouseScrolled(mouseX, mouseY, mouseZ, scroll);
    }

    protected void updateSearch(String searchString) {
        getMenu().updateFilter(searchString);
        this.scrollbar.setTotalRows(getTotalRows());
        this.scrollbar.scrollTo(0);
    }

    /**
     * The number of rows that the filtered elements occupy.
     *
     * This is rounded up, as a last row that is only partially filled
     * must still be reachable by the scrollbar.
     *
     * @return The number of rows.
     */
    protected int getTotalRows() {
        return Mth.ceil((double) getMenu().getFilteredItemCount() / getMenu().getColumns());
    }

    public EditBox getSearchField() {
        return searchField;
    }

    public WidgetScrollBar getScrollbar() {
        return scrollbar;
    }

    protected int getScrollX() {
        return 175;
    }

    protected int getScrollY() {
        return 18;
    }

    protected int getScrollHeight() {
        return 112;
    }

    protected boolean isSearchEnabled() {
        return true;
    }

    protected int getSearchX() {
        return 82;
    }

    protected int getSearchY() {
        return 6;
    }

    protected int getSearchWidth() {
        return SEARCH_WIDTH;
    }

}
