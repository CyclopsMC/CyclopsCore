package org.cyclops.cyclopscore.client.gui.component.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.RenderHelpers;

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

    protected void drawBackground(PoseStack matrixStack) {
        RenderHelpers.bindTexture(WIDGETS_LOCATION);

        int textureY = getTextureY();
        blit(matrixStack, getX(), getY(), 0, textureY, width / 2, height / 2); // top left
        blit(matrixStack, getX() + width / 2, getY(), 200 - width / 2, textureY, width / 2, height / 2); // top right
        blit(matrixStack, getX(), getY() + height / 2, 0, textureY + 20 - height / 2, width / 2, height / 2); // bottom left
        blit(matrixStack, getX() + width / 2, getY() + height / 2, 200 - width / 2, textureY + 20 - height / 2, width / 2, height / 2); // bottom right
    }

    @Override
    public void renderWidget(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            if(background) {
                drawBackground(matrixStack);
            }
            drawButtonInner(matrixStack, mouseX, mouseY);
        }
    }

    protected abstract void drawButtonInner(PoseStack matrixStack, int mouseX, int mouseY);

}
