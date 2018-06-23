package org.cyclops.cyclopscore.client.gui.container;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * Gui for an inventory container that has a scrollbar and search field.
 * @author rubensworks
 * TODO: rewrite with GuiScrollBar
 */
public abstract class ScrollingGuiContainer extends GuiContainerExtended {

    private static final ResourceLocation SCROLLBUTTON = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final int SCROLL_BUTTON_HEIGHT = 15;
    private static final int SCROLL_BUTTON_WIDTH = 12;
    private static final int SEARCH_WIDTH = 89;

    protected float currentScroll; // (0 = top, 1 = bottom)
    private boolean isScrolling; // if the scrollbar is being dragged
    private boolean wasClicking; // if the left mouse button was held down last time drawScreen was called

    private GuiTextField searchField = null;

    /**
     * Make a new instance.
     *
     * @param container The container to make the GUI for.
     */
    public ScrollingGuiContainer(ScrollingInventoryContainer<?> container) {
        super(container);
        this.allowUserInput = true;
    }

    protected ScrollingInventoryContainer<?> getScrollingInventoryContainer() {
        return (ScrollingInventoryContainer<?>) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        boolean resetFilter = false;
        if(isSearchEnabled()) {
            int searchWidth = getSearchWidth();
            int searchX = getSearchX();
            int searchY = getSearchY();
            if(this.searchField == null) {
                resetFilter = true;
                this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + searchX, this.guiTop + searchY, searchWidth, this.fontRenderer.FONT_HEIGHT);
                this.searchField.setMaxStringLength(64);
                this.searchField.setMaxStringLength(15);
                this.searchField.setEnableBackgroundDrawing(false);
                this.searchField.setVisible(true);
                this.searchField.setTextColor(16777215);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setText("");
                this.searchField.width = searchWidth;
                this.searchField.x = this.guiLeft + (searchX + searchWidth) - this.searchField.width;
            } else {
                this.searchField.width = searchWidth;
                this.searchField.x = this.guiLeft + (searchX + searchWidth) - this.searchField.width;
                this.searchField.y = this.guiTop + searchY;
            }
        }

        // Initial element load.
        if(resetFilter) {
            getScrollingInventoryContainer().updateFilter("");
        }
        getScrollingInventoryContainer().scrollTo(currentScroll);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isSearchEnabled() && !this.checkHotbarKeys(keyCode) && this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            this.updateSearch(searchField.getText());
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected int getScrollStep() {
        return getScrollingInventoryContainer().getFilteredItemCount() / getScrollingInventoryContainer().getColumns()
                - getScrollingInventoryContainer().getPageSize() + 1;
    }

    protected void scrollRelative(int step) {
        this.currentScroll = (float)((double)this.currentScroll - (double)step / (double)getScrollStep());
        this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
        getScrollingInventoryContainer().scrollTo(this.currentScroll);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            if (i > 0) i = 1;
            if (i < 0) i = -1;

            scrollRelative(i);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(isSearchEnabled()) {
            this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawCurrentScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + getScrollX();
        int j1 = l + getScrollY();
        int k1 = i1 + 14;
        int l1 = j1 + getScrollHeight();

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
            getScrollingInventoryContainer().scrollTo(this.currentScroll);
        }

        if (isSubsetRenderSlots()) {
            // Temporarily swap slot list, to avoid rendering all slots (which would include the hidden ones)
            List<Slot> oldSlots = this.inventorySlots.inventorySlots;
            int startIndex = getScrollingInventoryContainer().getFirstElement();
            List<Slot> newSlots = Lists.newArrayList();
            newSlots.addAll(oldSlots.subList(startIndex, Math.min(oldSlots.size(), startIndex
                    + (getScrollingInventoryContainer().getPageSize() * getScrollingInventoryContainer().getColumns()))));
            newSlots.addAll(oldSlots.subList(getScrollingInventoryContainer().getUnfilteredItemCount(), oldSlots.size()));
            this.inventorySlots.inventorySlots = newSlots;
            super.drawCurrentScreen(mouseX, mouseY, partialTicks);
            this.inventorySlots.inventorySlots = oldSlots;
        } else {
            super.drawCurrentScreen(mouseX, mouseY, partialTicks);
        }
    }

    /**
     * @return If the optimization should be done for only rendering the visible slots. Default: false
     */
    protected boolean isSubsetRenderSlots() {
        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(isSearchEnabled()) this.searchField.drawTextBox();
        int scrollX = this.guiLeft + getScrollX();
        int scrollMinY = this.guiTop + getScrollY();
        int scrollMaxY = scrollMinY + getScrollHeight();
        this.mc.getTextureManager().bindTexture(SCROLLBUTTON);
        this.drawTexturedModalRect(
                scrollX,
                scrollMinY + (int)((float)(scrollMaxY - scrollMinY - SCROLL_BUTTON_HEIGHT - 2) * this.currentScroll),
                232 + (this.needsScrollBars() ? 0 : SCROLL_BUTTON_WIDTH),
                0,
                SCROLL_BUTTON_WIDTH,
                SCROLL_BUTTON_HEIGHT
        );
    }

    protected void updateSearch(String searchString) {
        this.currentScroll = 0;
        getScrollingInventoryContainer().updateFilter(searchString);
    }

    protected boolean needsScrollBars() {
        return getScrollingInventoryContainer().getFilteredItemCount() > getScrollingInventoryContainer().getPageSize();
    }

    public GuiTextField getSearchField() {
        return searchField;
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
