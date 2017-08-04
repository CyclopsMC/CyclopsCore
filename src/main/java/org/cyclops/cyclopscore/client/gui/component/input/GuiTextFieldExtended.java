package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.cyclops.cyclopscore.client.gui.image.Images;

/**
 * An extended text field.
 * @author rubensworks
 */
public class GuiTextFieldExtended extends GuiTextField {

    private final boolean background;
    private IInputListener listener;

    public GuiTextFieldExtended(int componentId, FontRenderer fontrenderer, int x, int y, int width, int height, boolean background) {
        super(componentId, fontrenderer, x, y, width, height);
        this.background = background;
    }

    public GuiTextFieldExtended(int componentId, FontRenderer fontrenderer, int x, int y, int width, int height) {
        this(componentId, fontrenderer, x, y, width, height, false);
    }

    public void setListener(IInputListener listener) {
        this.listener = listener;
    }

    protected void drawBackground(Minecraft minecraft, int mouseX, int mouseY) {
        minecraft.renderEngine.bindTexture(Images.WIDGETS);
        GlStateManager.color(1, 1, 1, 1);

        x--;
        y--;
        drawTexturedModalRect(x, y, 0, 0, width / 2, height / 2);//top left
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 0, width / 2, height / 2);//top right
        drawTexturedModalRect(x, y + height / 2, 0, 20 - height / 2, width / 2, height / 2);//bottom left
        drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 20 - height / 2, width / 2, height / 2);//bottom right
        x++;
        y++;
    }

    @Override
    public void setText(String value) {
        super.setText(value);
        if(listener != null) listener.onChanged();
    }

    /**
     * Draw the text box with mouse sensitive parameters.
     * @param minecraft Minecraft instance.
     * @param mouseX Mouse X
     * @param mouseY Mouse Y
     */
    public void drawTextBox(Minecraft minecraft, int mouseX, int mouseY) {
        if(background) {
            drawBackground(minecraft, mouseX, mouseY);
        }
        super.drawTextBox();
    }

    @Deprecated
    public void drawTextBox() {
        super.drawTextBox();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 1) {
            // Select everything
            this.setCursorPosition(0);
            this.setSelectionPos(Integer.MAX_VALUE);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
