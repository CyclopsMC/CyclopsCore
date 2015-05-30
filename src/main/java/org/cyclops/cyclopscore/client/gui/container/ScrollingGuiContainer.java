package org.cyclops.cyclopscore.client.gui.container;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Gui for an inventory container that has a scrollbar and search field.
 * @author rubensworks
 */
public abstract class ScrollingGuiContainer extends GuiContainerExtended {

    private static final ResourceLocation SCROLLBUTTON = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private static final int SCROLL_BUTTON_HEIGHT = 15;
    private static final int SCROLL_BUTTON_WIDTH = 12;
    private static final int SEARCH_WIDTH = 89;

    private float currentScroll; // (0 = top, 1 = bottom)
    private boolean isScrolling; // if the scrollbar is being dragged
    private boolean wasClicking; // if the left mouse button was held down last time drawScreen was called

    private GuiTextField searchField;

    /**
     * Make a new instance.
     *
     * @param container The container to make the GUI for.
     */
    public ScrollingGuiContainer(ScrollingInventoryContainer container) {
        super(container);
        this.allowUserInput = true;
    }

    protected ScrollingInventoryContainer getScrollingInventoryContainer() {
        return (ScrollingInventoryContainer) this.inventorySlots;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        if(isSearchEnabled()) {
            int searchWidth = getSearchWidth();
            int searchX = getSearchX();
            int searchY = getSearchY();
            this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + searchX, this.guiTop + searchY, searchWidth, this.fontRendererObj.FONT_HEIGHT);
            this.searchField.setMaxStringLength(64);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(true);
            this.searchField.setTextColor(16777215);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setText("");
            this.searchField.width = searchWidth;
            this.searchField.xPosition = this.guiLeft + (searchX + searchWidth) - this.searchField.width;
        }

        // Initial element load.
        getScrollingInventoryContainer().updateFilter("");
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isSearchEnabled() && !this.checkHotbarKeys(keyCode)) {
            if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                this.updateSearch(searchField.getText());
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0 && this.needsScrollBars()) {
            int j = getScrollingInventoryContainer().getFilteredItemCount() / 9 - 5 + 1;

            if (i > 0) i = 1;
            if (i < 0) i = -1;

            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            getScrollingInventoryContainer().scrollTo(this.currentScroll);
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + 175;
        int j1 = l + 18;
        int k1 = i1 + 14;
        int l1 = j1 + 112;

        if (!this.wasClicking && flag && mouseX >= i1 && mouseY >= j1 && mouseX < k1 && mouseY < l1) {
            this.isScrolling = this.needsScrollBars();
        }

        if (!flag) {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling) {
            this.currentScroll = ((float)(mouseY - j1) - 7.5F) / ((float)(l1 - j1) - 15.0F);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0F, 1.0F);
            getScrollingInventoryContainer().scrollTo(this.currentScroll);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if(isSearchEnabled()) this.searchField.drawTextBox();
        int scrollX = this.guiLeft + 175;
        int scrollMinY = this.guiTop + 18;
        int scrollMaxY = scrollMinY + 112;
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
        getScrollingInventoryContainer().updateFilter(searchString);
    }

    protected boolean needsScrollBars() {
        return getScrollingInventoryContainer().getFilteredItemCount() > getScrollingInventoryContainer().getPageSize();
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
