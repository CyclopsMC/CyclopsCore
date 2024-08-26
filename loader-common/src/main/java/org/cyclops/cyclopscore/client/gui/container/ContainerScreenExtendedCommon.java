package org.cyclops.cyclopscore.client.gui.container;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.container.ContainerExtendedCommon;

import java.awt.*;

/**
 * An extended GUI container.
 * @author rubensworks
 */
public abstract class ContainerScreenExtendedCommon<T extends ContainerExtendedCommon> extends AbstractContainerScreen<T> {

    private final IModHelpers modHelpers;

    protected T container;
    protected ResourceLocation texture;
    protected int offsetX = 0;
    protected int offsetY = 0;

    public ContainerScreenExtendedCommon(T container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.modHelpers = IModHelpers.get();
        this.container = container;
        this.texture = constructGuiTexture();
    }

    protected abstract ResourceLocation constructGuiTexture();

    /**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this block.
     */
    public ResourceLocation getGuiTexture() {
        return this.texture;
    }

    @Override
    public void init() {
        this.imageWidth = getBaseXSize() + offsetX * 2;
        this.imageHeight = getBaseYSize() + offsetY * 2;
        super.init();
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        this.drawCurrentScreen(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    protected void drawCurrentScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    protected int getBaseXSize() {
        return 176;
    }

    protected int getBaseYSize() {
        return 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int x, int y) {
        guiGraphics.blit(getGuiTexture(), leftPos + offsetX, topPos + offsetY, 0, 0, imageWidth - 2 * offsetX, imageHeight - 2 * offsetY);
    }

    // @Override // This is an override in Forge and NeoForge, but not in Fabric
    public boolean isHovering(Slot slotIn, double mouseX, double mouseY) {
        return this.isHovering(slotIn.x - 1, slotIn.y - 1,
                18, 18, mouseX, mouseY);
    }

    @Override
    public boolean isHovering(int left, int top, int right, int bottom, double pointX, double pointY) {
        return this.modHelpers.getRenderHelpers().isPointInRegion(left, top, right, bottom, pointX - this.leftPos, pointY - this.topPos);
    }

    public boolean isPointInRegion(Rectangle region, Point mouse) {
        return isHovering(region.x, region.y, region.width, region.height, mouse.x, mouse.y);
    }

    /**
     * @return The total gui left offset.
     */
    public int getGuiLeftTotal() {
        return this.leftPos + offsetX;
    }

    /**
     * @return The total gui top offset.
     */
    public int getGuiTopTotal() {
        return this.topPos + offsetY;
    }
}
