package org.cyclops.cyclopscore.client.gui.container;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.container.ContainerExtended;
import org.cyclops.cyclopscore.inventory.container.button.IContainerButtonAction;
import org.cyclops.cyclopscore.network.packet.ButtonClickPacket;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

/**
 * An extended GUI container.
 * @author rubensworks
 */
public abstract class ContainerScreenExtended<T extends ContainerExtended> extends ContainerScreen<T>
        implements IValueNotifiable {

    protected T container;
    protected ResourceLocation texture;
    protected int offsetX = 0;
    protected int offsetY = 0;

    public ContainerScreenExtended(T container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        container.setGuiValueListener(this);
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
        this.xSize = getBaseXSize() + offsetX * 2;
        this.ySize = getBaseYSize() + offsetY * 2;
        super.init();
    }

    @Override
    public final void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.drawCurrentScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawCurrentScreen(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
    }

    protected int getBaseXSize() {
        return 176;
    }

    protected int getBaseYSize() {
        return 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelpers.bindTexture(texture);
        blit(guiLeft + offsetX, guiTop + offsetY, 0, 0, xSize - 2 * offsetX, ySize - 2 * offsetY);
    }

    @Override
    public boolean isSlotSelected(Slot slotIn, double mouseX, double mouseY) {
        return this.isPointInRegion(slotIn.xPos - 1, slotIn.yPos - 1,
                GuiHelpers.SLOT_SIZE, GuiHelpers.SLOT_SIZE, mouseX, mouseY);
    }

    @Override
    public boolean isPointInRegion(int left, int top, int right, int bottom, double pointX, double pointY) {
        return RenderHelpers.isPointInRegion(left, top, right, bottom, pointX - this.guiLeft, pointY - this.guiTop);
    }

    public boolean isPointInRegion(Rectangle region, Point mouse) {
    	return isPointInRegion(region.x, region.y, region.width, region.height, mouse.x, mouse.y);
    }

    public void drawTooltip(List<ITextComponent> lines, int x, int y) {
        GuiHelpers.drawTooltip(this, lines, x, y);
    }

    /**
     * Call this to create a button pressable callback so that the container is notified as well,
     * assuming it has a corresponding registered {@link IContainerButtonAction} registered in the container
     * by the same button id.
     * @param buttonId The button id.
     * @param clientPressable An optional pressable that should be called client-side.
     * @return The created pressable.
     */
    protected Button.IPressable createServerPressable(String buttonId, @Nullable Button.IPressable clientPressable) {
        return (button) -> {
            if (clientPressable != null) {
                clientPressable.onPress(button);
            }
            if (getContainer().onButtonClick(buttonId)) {
                CyclopsCore._instance.getPacketHandler().sendToServer(new ButtonClickPacket(buttonId));
            }
        };
    }

    @Override
    public void onUpdate(int valueId, CompoundNBT value) {

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

    @Override
    public ContainerType<?> getType() {
        return getContainer().getType();
    }
}
