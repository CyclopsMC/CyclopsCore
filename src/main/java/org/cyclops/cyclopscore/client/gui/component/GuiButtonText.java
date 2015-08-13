package org.cyclops.cyclopscore.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * An button with text.
 * @author rubensworks
 *
 */
public class GuiButtonText extends GuiButtonExtended {

	/**
	 * Make a new instance.
	 * @param id The ID.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param string The string to print.
     * @param background If the button background should be rendered.
	 */
	public GuiButtonText(int id, int x, int y,
						 int width, int height, String string,
                         boolean background) {
		super(id, x, y, width, height, string, background);
	}

	protected void drawButtonInner(Minecraft minecraft, int i, int j, boolean mouseOver) {
        FontRenderer fontrenderer = minecraft.fontRendererObj;

        int color = 0xe0e0e0;
        if(!enabled) {
            color = 0xffa0a0a0;
        } else if (mouseOver) {
            color = 0xffffa0;
        }

        drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, color);
    }

}
