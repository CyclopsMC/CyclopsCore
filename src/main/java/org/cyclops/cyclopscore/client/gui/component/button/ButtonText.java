package org.cyclops.cyclopscore.client.gui.component.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * An button with text.
 * @author rubensworks
 *
 */
public class ButtonText extends ButtonExtended {

	private final Component text;

    /**
     * Make a new instance.
     * @param x X
     * @param y Y
     * @param narrationMessage The string to print.
	 * @param text The text to print.
	 * @param pressCallback A callback for when this button was pressed.
     */
    public ButtonText(int x, int y, Component narrationMessage, Component text, Button.OnPress pressCallback) {
    	// MCP: getStringWidth
        this(x, y, Minecraft.getInstance().font.width(text.getVisualOrderText()) + 6, 16, narrationMessage, text, pressCallback, true);
    }

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param narrationMessage The string to print.
	 * @param text The text to print.
     * @param pressCallback A callback for when this button was pressed.
	 * @param background If the button background should be rendered.
	 */
	public ButtonText(int x, int y, int width, int height, Component narrationMessage, Component text,
					  Button.OnPress pressCallback, boolean background) {
		super(x, y, width, height, narrationMessage, pressCallback, background);
		this.text = text;
	}

	public Component getText() {
		return text;
	}

	@Override
	protected void drawButtonInner(PoseStack matrixStack, int mouseX, int mouseY) {
        int color = 0xe0e0e0;
        if (!active) {
            color = 0xffa0a0a0;
        } else if (isHoveredOrFocused()) {
            color = 0xffffa0;
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().font, getText(), x + width / 2, y + (height - 8) / 2, color);
    }

}
