package org.cyclops.cyclopscore.client.gui.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.cyclopscore.client.gui.component.WidgetScrollBar;
import org.cyclops.cyclopscore.client.gui.component.input.WidgetTextFieldExtended;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

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
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        if(isSearchEnabled()) {
            int searchWidth = getSearchWidth();
            int searchX = getSearchX();
            int searchY = getSearchY();
            if(this.searchField == null) {
                this.searchField = new WidgetTextFieldExtended(this.font, this.leftPos + searchX, this.topPos + searchY, searchWidth, this.font.lineHeight, new TranslatableComponent("gui.cyclopscore.search"));
                this.searchField.setMaxLength(64);
                this.searchField.setMaxLength(15);
                this.searchField.setBordered(false);
                this.searchField.setVisible(true);
                this.searchField.setTextColor(16777215);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setValue("");
                this.searchField.setWidth(searchWidth);
                this.searchField.x = this.leftPos + (searchX + searchWidth) - this.searchField.getWidth();
            } else {
                this.searchField.setWidth(searchWidth);
                this.searchField.x = this.leftPos + (searchX + searchWidth) - this.searchField.getWidth();
                this.searchField.y = this.topPos + searchY;
            }
            this.addWidget(this.searchField);
        }

        // Initial element load.
        if (scrollbar == null) {
            getMenu().updateFilter("");
            this.scrollbar = new WidgetScrollBar(this.leftPos + getScrollX(), this.topPos + getScrollY(), getScrollHeight(),
                    new TranslatableComponent("gui.cyclopscore.scrollbar"), getMenu(),
                    getMenu().getPageSize(), getScrollRegion());
            this.scrollbar.setTotalRows(getMenu().getFilteredItemCount() / getMenu().getColumns());
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
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.searchField != null) {
            this.searchField.tick();
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (isSearchEnabled() && this.searchField.isFocused()) {
            if (this.searchField.charTyped(typedChar, keyCode)) {
                this.updateSearch(searchField.getValue());
            }
            return true;
        } else {
            return super.charTyped(typedChar, keyCode);
        }
    }

    @Override
    public boolean keyPressed(int typedChar, int keyCode, int modifiers) {
        if (isSearchEnabled() && this.searchField.isFocused() && typedChar != GLFW.GLFW_KEY_ESCAPE) {
            if (this.searchField.keyPressed(typedChar, keyCode, modifiers)) {
                this.updateSearch(searchField.getValue());
            }
            return true;
        } else {
            return super.keyPressed(typedChar, keyCode, modifiers);
        }
    }

    @Override
    protected void drawCurrentScreen(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (isSubsetRenderSlots()) {
            // Temporarily swap slot list, to avoid rendering all slots (which would include the hidden ones)
            List<Slot> oldSlots = Lists.newArrayList(this.container.slots);
            int startIndex = getMenu().getFirstElement();
            List<Slot> newSlots = Lists.newArrayList();
            newSlots.addAll(oldSlots.subList(startIndex, Math.min(oldSlots.size(), startIndex
                    + (getMenu().getPageSize() * getMenu().getColumns()))));
            newSlots.addAll(oldSlots.subList(getMenu().getUnfilteredItemCount(), oldSlots.size()));
            this.container.slots.clear();
            this.container.slots.addAll(newSlots);
            super.drawCurrentScreen(matrixStack, mouseX, mouseY, partialTicks);
            this.container.slots.clear();
            this.container.slots.addAll(oldSlots);
        } else {
            super.drawCurrentScreen(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * @return If the optimization should be done for only rendering the visible slots. Default: false
     */
    protected boolean isSubsetRenderSlots() {
        return false;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
        if(isSearchEnabled()) this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);
        this.scrollbar.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double mouseXPrev, double mouseYPrev) {
        if (this.getFocused() != null && this.isDragging() && mouseButton == 0
                && this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, mouseXPrev, mouseYPrev)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, mouseButton, mouseXPrev, mouseYPrev);
    }

    protected void updateSearch(String searchString) {
        getMenu().updateFilter(searchString);
        this.scrollbar.setTotalRows(getMenu().getFilteredItemCount() / getMenu().getColumns());
        this.scrollbar.scrollTo(0);
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
