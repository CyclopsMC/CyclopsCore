package org.cyclops.cyclopscore.client.gui.component.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.cyclopscore.helper.RenderHelpers;

/**
 * An extended {@link Button} that is better resizable.
 * @author rubensworks
 *
 */
public abstract class ButtonExtended extends Button {

    private final boolean background;

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param narrationMessage The string to print.
	 * @param pressCallback A callback for when this button was pressed.
     * @param background If the background of the button should be rendered.
	 */
	public ButtonExtended(int x, int y, int width, int height, ITextComponent narrationMessage,
						  Button.IPressable pressCallback, boolean background) {
		super(x, y, width, height, narrationMessage, pressCallback);
        this.background = background;
	}

	protected void drawBackground(MatrixStack matrixStack) {
		RenderHelpers.bindTexture(WIDGETS_LOCATION);
		GlStateManager._color4f(1, 1, 1, 1);

		int hoverState = getYImage(isHovered());
		blit(matrixStack, x, y, 0, 46 + hoverState * 20, width / 2, height / 2); // top left
		blit(matrixStack, x + width / 2, y, 200 - width / 2, 46 + hoverState * 20, width / 2, height / 2); // top right
		blit(matrixStack, x, y + height / 2, 0, 46 + hoverState * 20 + 20 - height / 2, width / 2, height / 2); // bottom left
		blit(matrixStack, x + width / 2, y + height / 2, 200 - width / 2, 46 + hoverState * 20 + 20 - height / 2, width / 2, height / 2); // bottom right
	}
	
	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            if(background) {
                drawBackground(matrixStack);
            }
			drawButtonInner(matrixStack, mouseX, mouseY);
        }
    }

	protected abstract void drawButtonInner(MatrixStack matrixStack, int mouseX, int mouseY);

}
