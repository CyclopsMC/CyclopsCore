package org.cyclops.cyclopscore.client.gui.component.button;

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
     * @param string The string to print.
     */
    public GuiButtonText(int id, int x, int y,
                         String string) {
        this(id, x, y, Minecraft.getMinecraft().fontRenderer.getStringWidth(string) + 6, 16, string, true);
    }

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
        FontRenderer fontrenderer = minecraft.fontRenderer;

        int color = 0xe0e0e0;
        if(!enabled) {
            color = 0xffa0a0a0;
        } else if (mouseOver) {
            color = 0xffffa0;
        }

        drawCenteredString(fontrenderer, displayString, x + width / 2, y + (height - 8) / 2, color);
    }

}
