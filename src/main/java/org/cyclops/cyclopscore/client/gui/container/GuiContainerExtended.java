package org.cyclops.cyclopscore.client.gui.container;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.container.ExtendedInventoryContainer;
import org.cyclops.cyclopscore.inventory.container.button.IButtonActionClient;
import org.cyclops.cyclopscore.inventory.container.button.IButtonClickAcceptorClient;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.packet.ButtonClickPacket;
import org.lwjgl.opengl.GL11;

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
        GlStateManager.pushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        
        int tooltipWidth = 0;
        int tempWidth;
        int xStart;
        int yStart;
        
        for(String line : lines) {
            tempWidth = this.fontRenderer.getStringWidth(line);
            
            if(tempWidth > tooltipWidth) {
                tooltipWidth = tempWidth;
            }
        }
        
        xStart = x + 12;
        yStart = y - 12;
        int tooltipHeight = 8;
        
        if(lines.size() > 1) {
            tooltipHeight += 2 + (lines.size() - 1) * 10;
        }

        if(this.guiLeft + xStart + tooltipWidth + 6 > this.width) {
            xStart = this.width - tooltipWidth - this.guiLeft - 6;
        }
        
        if(this.guiTop + yStart + tooltipHeight + 6 > this.height) {
            yStart = this.height - tooltipHeight - this.guiTop - 6;
        }
        
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int color1 = -267386864;
        this.drawGradientRect(xStart - 3, yStart - 4, xStart + tooltipWidth + 3, yStart - 3, color1, color1);
        this.drawGradientRect(xStart - 3, yStart + tooltipHeight + 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 4, color1, color1);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color1, color1);
        this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + tooltipHeight + 3, color1, color1);
        this.drawGradientRect(xStart + tooltipWidth + 3, yStart - 3, xStart + tooltipWidth + 4, yStart + tooltipHeight + 3, color1, color1);
        int color2 = 1347420415;
        int color3 = (color2 & 16711422) >> 1 | color2 & -16777216;
        this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipHeight + 3 - 1, color2, color3);
        this.drawGradientRect(xStart + tooltipWidth + 2, yStart - 3 + 1, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3 - 1, color2, color3);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart - 3 + 1, color2, color2);
        this.drawGradientRect(xStart - 3, yStart + tooltipHeight + 2, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color3, color3);
        
        for(int stringIndex = 0; stringIndex < lines.size(); ++stringIndex) {
            String line = lines.get(stringIndex);
            
            if(stringIndex == 0) {
                line = "\u00a7" + Integer.toHexString(15) + line;
            } else {
                line = "\u00a77" + line;
            }
            
            this.fontRenderer.drawStringWithShadow(line, xStart, yStart, -1);
            
            if(stringIndex == 0) {
                yStart += 2;
            }
            
            yStart += 10;
        }
        
        GlStateManager.popMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
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
     * @return The total gui left offset.
     */
    public int getGuiLeft() {
        return this.guiLeft + offsetX;
    }

    /**
     * @return The total gui top offset.
     */
    public int getGuiTop() {
        return this.guiTop + offsetY;
    }

}
