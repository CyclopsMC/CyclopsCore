package org.cyclops.cyclopscore.client.gui.component;

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
	
	@Override
	public void drawButton(Minecraft minecraft, int i, int j) {
        if(visible) {
	        minecraft.renderEngine.bindTexture(buttonTextures);
	        GlStateManager.color(1, 1, 1, 1);
	        
	        boolean mouseOver = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
	        int k = getHoverState(mouseOver);

            if(background) {
                drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height / 2);//top left
                drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height / 2);//top right
                drawTexturedModalRect(xPosition, yPosition + height / 2, 0, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
                drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
            }
			drawButtonInner(minecraft, i, j, mouseOver);

	        mouseDragged(minecraft, i, j);
        }
    }

	protected abstract void drawButtonInner(Minecraft minecraft, int i, int j, boolean mouseOver);

}
