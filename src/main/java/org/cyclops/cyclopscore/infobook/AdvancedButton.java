package org.cyclops.cyclopscore.infobook;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

    private Button.IPressable onPress;

    public AdvancedButton() {
        super(0, 0, 0, 0, new StringTextComponent(""), null);
    }

    public void setOnPress(IPressable onPress) {
        this.onPress = onPress;
    }

    public IPressable getOnPress() {
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
    public void update(int x, int y, ITextComponent displayName, InfoSection target, ScreenInfoBook gui) {
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
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(isVisible() && isHover(mouseX, mouseY)) {
            Minecraft.getInstance().fontRenderer.drawString(matrixStack, ("§n") + getMessage() + "§r", x, y,
                    Helpers.RGBToInt(100, 100, 150));
        }
    }

    /**
     * Render the button tooltip.
     * @param matrixStack The matrix stack.
     * @param mx Mouse x.
     * @param my Mouse Y.
     */
    public void renderTooltip(MatrixStack matrixStack, int mx, int my) {

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
