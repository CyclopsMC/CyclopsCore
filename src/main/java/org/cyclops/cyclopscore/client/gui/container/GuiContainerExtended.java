package org.cyclops.cyclopscore.client.gui.container;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.container.ExtendedInventoryContainer;
import org.cyclops.cyclopscore.inventory.container.button.IButtonActionClient;
import org.cyclops.cyclopscore.inventory.container.button.IButtonClickAcceptorClient;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.packet.ButtonClickPacket;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * An extended GUI container.
 * @author rubensworks
 */
public abstract class GuiContainerExtended extends GuiContainer implements
        IButtonClickAcceptorClient<GuiContainerExtended, ExtendedInventoryContainer>, IValueNotifiable {

    private final Map<Integer, IButtonActionClient<GuiContainerExtended, ExtendedInventoryContainer>> buttonActions = Maps.newHashMap();

    protected ExtendedInventoryContainer container;
    protected ResourceLocation texture;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /**
     * Make a new instance.
     * @param container The container to make the GUI for.
     */
    public GuiContainerExtended(ExtendedInventoryContainer container) {
        super(container);
        container.setGuiValueListener(this);
        this.container = container;
        this.texture = constructResourceLocation();
    }

    protected ExtendedInventoryContainer getContainer() {
        return this.container;
    }

    protected ResourceLocation constructResourceLocation() {
        return new ResourceLocation(container.getGuiProvider().getModGui().getModId(), getGuiTexture());
    }

    /**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this block.
     */
    public abstract String getGuiTexture();

    @Override
    public void initGui() {
        this.xSize = getBaseXSize() + offsetX * 2;
        this.ySize = getBaseYSize() + offsetY * 2;
        super.initGui();
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCurrentScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawCurrentScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected int getBaseXSize() {
        return 176;
    }

    protected int getBaseYSize() {
        return 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft + offsetX, guiTop + offsetY, 0, 0, xSize - 2 * offsetX, ySize - 2 * offsetY);
    }

    @Override
    public boolean isPointInRegion(int left, int top, int right, int bottom, int pointX, int pointY) {
        return RenderHelpers.isPointInRegion(left, top, right, bottom, pointX - this.guiLeft, pointY - this.guiTop);
    }

    public boolean isPointInRegion(Rectangle region, Point mouse) {
    	return isPointInRegion(region.x, region.y, region.width, region.height, mouse.x, mouse.y);
    }

    public void drawTooltip(List<String> lines, int x, int y) {
        GuiHelpers.drawTooltip(this, lines, x, y);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long time) {
        Slot slot = getSlotUnderMouse();
        if (mouseButton == 1 && slot instanceof SlotExtended && ((SlotExtended) slot).isPhantom()) {
            return;
        }
        super.mouseClickMove(mouseX, mouseY, mouseButton, time);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(requiresAction(button.id)) {
            onButtonClick(button.id);
        }
        if(getContainer().requiresAction(button.id)) {
            getContainer().onButtonClick(button.id);
            CyclopsCore._instance.getPacketHandler().sendToServer(new ButtonClickPacket(button.id));
        }
    }

    @Override
    public void putButtonAction(int buttonId, IButtonActionClient<GuiContainerExtended, ExtendedInventoryContainer> action) {
        buttonActions.put(buttonId, action);
    }

    @Override
    public boolean requiresAction(int buttonId) {
        return buttonActions.containsKey(buttonId);
    }

    @Override
    public void onButtonClick(int buttonId) {
        IButtonActionClient<GuiContainerExtended, ExtendedInventoryContainer> action;
        if((action = buttonActions.get(buttonId)) != null) {
            action.onAction(buttonId, this, getContainer());
        }
    }

    @Override
    public void onUpdate(int valueId, NBTTagCompound value) {

    }

    /**
     * Will send client-side onUpdate events for all stored values
     */
    protected void refreshValues() {
        for (int id : getContainer().getValueIds()) {
            onUpdate(id, getContainer().getValue(id));
        }
    }

    /**
     * @return The total gui left offset.
     */
    public int getGuiLeftTotal() {
        return this.guiLeft + offsetX;
    }

    /**
     * @return The total gui top offset.
     */
    public int getGuiTopTotal() {
        return this.guiTop + offsetY;
    }

}
