package org.cyclops.cyclopscore.client.gui.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.client.gui.component.WidgetScrollBar;
import org.cyclops.cyclopscore.client.gui.component.input.WidgetTextFieldExtended;
import org.cyclops.cyclopscore.helper.L10NHelpers;
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

    public ContainerScreenScrolling(T container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        if(isSearchEnabled()) {
            int searchWidth = getSearchWidth();
            int searchX = getSearchX();
            int searchY = getSearchY();
            if(this.searchField == null) {
                this.searchField = new WidgetTextFieldExtended(this.font, this.guiLeft + searchX, this.guiTop + searchY, searchWidth, this.font.FONT_HEIGHT, new TranslationTextComponent("gui.cyclopscore.search"));
                this.searchField.setMaxStringLength(64);
                this.searchField.setMaxStringLength(15);
                this.searchField.setEnableBackgroundDrawing(false);
                this.searchField.setVisible(true);
                this.searchField.setTextColor(16777215);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setText("");
                this.searchField.setWidth(searchWidth);
                this.searchField.x = this.guiLeft + (searchX + searchWidth) - this.searchField.getWidth();
            } else {
                this.searchField.setWidth(searchWidth);
                this.searchField.x = this.guiLeft + (searchX + searchWidth) - this.searchField.getWidth();
                this.searchField.y = this.guiTop + searchY;
            }
            this.children.add(this.searchField);
        }

        // Initial element load.
        if (scrollbar == null) {
            getContainer().updateFilter("");
            this.scrollbar = new WidgetScrollBar(this.guiLeft + getScrollX(), this.guiTop + getScrollY(), getScrollHeight(),
                    new TranslationTextComponent("gui.cyclopscore.scrollbar"), getContainer(),
                    getContainer().getPageSize(), getScrollRegion());
            this.scrollbar.setTotalRows(getContainer().getFilteredItemCount() / getContainer().getColumns());
        }

        this.children.add(this.scrollbar);
        getScrollbar().scrollTo(this.scrollbar.getCurrentScroll());
    }

    /**
     * @return A custom region in which scrolling should also be allowed next to the scrollbar itself.
     */
    protected Rectangle getScrollRegion() {
        return null;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.searchField != null) {
            this.searchField.tick();
        }
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (isSearchEnabled() && this.searchField.isFocused()) {
            if (this.searchField.charTyped(typedChar, keyCode)) {
                this.updateSearch(searchField.getText());
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
                this.updateSearch(searchField.getText());
            }
            return true;
        } else {
            return super.keyPressed(typedChar, keyCode, modifiers);
        }
    }

    @Override
    protected void drawCurrentScreen(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (isSubsetRenderSlots()) {
            // Temporarily swap slot list, to avoid rendering all slots (which would include the hidden ones)
            List<Slot> oldSlots = Lists.newArrayList(this.container.inventorySlots);
            int startIndex = getContainer().getFirstElement();
            List<Slot> newSlots = Lists.newArrayList();
            newSlots.addAll(oldSlots.subList(startIndex, Math.min(oldSlots.size(), startIndex
                    + (getContainer().getPageSize() * getContainer().getColumns()))));
            newSlots.addAll(oldSlots.subList(getContainer().getUnfilteredItemCount(), oldSlots.size()));
            this.container.inventorySlots.clear();
            this.container.inventorySlots.addAll(newSlots);
            super.drawCurrentScreen(matrixStack, mouseX, mouseY, partialTicks);
            this.container.inventorySlots.clear();
            this.container.inventorySlots.addAll(oldSlots);
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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        if(isSearchEnabled()) this.searchField.render(matrixStack, mouseX, mouseY, partialTicks);
        this.scrollbar.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double mouseXPrev, double mouseYPrev) {
        if (this.getListener() != null && this.isDragging() && mouseButton == 0
                && this.getListener().mouseDragged(mouseX, mouseY, mouseButton, mouseXPrev, mouseYPrev)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, mouseButton, mouseXPrev, mouseYPrev);
    }

    protected void updateSearch(String searchString) {
        getContainer().updateFilter(searchString);
        this.scrollbar.setTotalRows(getContainer().getFilteredItemCount() / getContainer().getColumns());
        this.scrollbar.scrollTo(0);
    }

    public TextFieldWidget getSearchField() {
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
