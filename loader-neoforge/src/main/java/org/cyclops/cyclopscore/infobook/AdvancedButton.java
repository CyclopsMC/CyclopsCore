package org.cyclops.cyclopscore.infobook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * An advanced button type.
 * @author rubensworks
 */
public class AdvancedButton extends Button {

    private InfoSection target;
    protected ScreenInfoBook gui;

    private Button.OnPress onPress;

    public AdvancedButton() {
        super(0, 0, 0, 0, Component.literal(""), null, Button.DEFAULT_NARRATION);
    }

    public void setOnPress(OnPress onPress) {
        this.onPress = onPress;
    }

    public OnPress getOnPress() {
        return this.onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
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
        setX(x);
        setY(y);
        this.setMessage(displayName);
        this.target = target;
        this.gui = gui;
        this.width = 16;
        this.height = 16;
        this.active = isVisible();
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(isVisible() && isHover(mouseX, mouseY)) {
            guiGraphics.text(Minecraft.getInstance().font, getMessage(), getX(), getY(), IModHelpers.get().getBaseHelpers().RGBAToInt(100, 100, 150, 255), false);
        }
    }

    /**
     * Render the button tooltip.
     * @param guiGraphics The gui graphics object.
     * @param font The font.
     * @param mx Mouse x.
     * @param my Mouse Y.
     */
    public void renderTooltip(GuiGraphicsExtractor guiGraphics, Font font, int mx, int my) {

    }

    protected boolean isHover(int mouseX, int mouseY) {
        return mouseX >= this.getX() && mouseY >= this.getY() &&
                mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
    }

    public boolean isVisible() {
        return this.visible && getTarget() != null;
    }

    public InfoSection getTarget() {
        return this.target;
    }
}
