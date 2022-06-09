package org.cyclops.cyclopscore.infobook;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * An advanced button type.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class AdvancedButton extends Button {

    private InfoSection target;
    protected ScreenInfoBook gui;

    private Button.OnPress onPress;

    public AdvancedButton() {
        super(0, 0, 0, 0, Component.literal(""), null);
    }

    public void setOnPress(OnPress onPress) {
        this.onPress = onPress;
    }

    public OnPress getOnPress() {
        return this.onPress;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) {
            this.onPress.onPress(this);
        }
    }

    /**
     * This is called each render tick to update the button to the latest render state.
     * @param x The X position.
     * @param y The Y position.
     * @param displayName The text to display.
     * @param target The target section.
     * @param gui The gui.
     */
    public void update(int x, int y, Component displayName, InfoSection target, ScreenInfoBook gui) {
        this.x = x;
        this.y = y;
        this.setMessage(displayName);
        this.target = target;
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.active = isVisible();
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(isVisible() && isHover(mouseX, mouseY)) {
            // MCP: drawString
            Minecraft.getInstance().font.draw(matrixStack, ((MutableComponent) getMessage()).withStyle(ChatFormatting.UNDERLINE), x, y,
                    Helpers.RGBToInt(100, 100, 150));
        }
    }

    /**
     * Render the button tooltip.
     * @param matrixStack The matrix stack.
     * @param mx Mouse x.
     * @param my Mouse Y.
     */
    public void renderTooltip(PoseStack matrixStack, int mx, int my) {

    }

    protected boolean isHover(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y &&
                mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    public boolean isVisible() {
        return this.visible && getTarget() != null;
    }

    public InfoSection getTarget() {
        return this.target;
    }
}
