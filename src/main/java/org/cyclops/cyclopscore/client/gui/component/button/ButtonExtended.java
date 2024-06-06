package org.cyclops.cyclopscore.client.gui.component.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * An extended {@link Button} that is better resizable.
 * @author rubensworks
 *
 */
public abstract class ButtonExtended extends Button {

    private final boolean background;

    public ButtonExtended(int x, int y, int width, int height, Component narrationMessage,
                          Button.OnPress pressCallback, boolean background) {
        this(x, y, width, height, narrationMessage, pressCallback, background, DEFAULT_NARRATION);
    }

    public ButtonExtended(int x, int y, int width, int height, Component narrationMessage,
                          Button.OnPress pressCallback, boolean background, Button.CreateNarration createNarration) {
        super(x, y, width, height, narrationMessage, pressCallback, createNarration);
        this.background = background;
    }

    protected int getYImage() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return i;
    }

    protected int getTextureY() { // Copy from AbstractButton
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }

    protected void drawBackground(GuiGraphics guiGraphics) {
        ResourceLocation resourceLocation = SPRITES.get(this.active, this.isHoveredOrFocused());
        guiGraphics.blitSprite(resourceLocation, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            if(background) {
                drawBackground(guiGraphics);
            }
            drawButtonInner(guiGraphics, mouseX, mouseY);
        }
    }

    protected abstract void drawButtonInner(GuiGraphics guiGraphics, int mouseX, int mouseY);

}
