package org.cyclops.cyclopscore.client.gui.component.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * An extended {@link net.minecraft.client.gui.GuiButton} which is better resizable.
 * Based on chickenbones' GuiNEIButton.
 * @author rubensworks
 *
 */
public abstract class GuiButtonExtended extends GuiButton {

    private final boolean background;

	/**
	 * Make a new instance.
	 * @param id The ID.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param string The string to print.
     * @param background If the background of the button should be rendered.
	 */
	public GuiButtonExtended(int id, int x, int y,
                             int width, int height, String string, boolean background) {
		super(id, x, y, width, height, string);
        this.background = background;
	}

	protected void drawBackground(Minecraft minecraft, int hoverState) {
		minecraft.renderEngine.bindTexture(BUTTON_TEXTURES);
		GlStateManager.color(1, 1, 1, 1);

		drawTexturedModalRect(x, y, 0, 46 + hoverState * 20, width / 2, height / 2);//top left
		drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + hoverState * 20, width / 2, height / 2);//top right
		drawTexturedModalRect(x, y + height / 2, 0, 46 + hoverState * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
		drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 46 + hoverState * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int i, int j, float partialTicks) {
        if(visible) {
	        boolean mouseOver = i >= x && j >= y && i < x + width && j < y + height;
	        int hoverState = getHoverState(mouseOver);
            if(background) {
                drawBackground(minecraft, hoverState);
            }
			drawButtonInner(minecraft, i, j, mouseOver);

	        mouseDragged(minecraft, i, j);
        }
    }

	protected abstract void drawButtonInner(Minecraft minecraft, int i, int j, boolean mouseOver);

}
