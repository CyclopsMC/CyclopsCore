package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.cyclops.cyclopscore.client.gui.component.button.GuiButtonArrow;

/**
 * A number field which by default only accepts positive numbers.
 * @author rubensworks
 */
public class GuiNumberField extends GuiTextFieldExtended {

    private final boolean arrows;
    private GuiButtonArrow arrowUp;
    private GuiButtonArrow arrowDown;
    private boolean positiveOnly = true;

    public GuiNumberField(int componentId, FontRenderer fontrenderer, int x, int y, int width, int height, boolean arrows, boolean background) {
        super(componentId, fontrenderer, x, y, width, height, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowUp = new GuiButtonArrow(0, x, y + height / 2, GuiButtonArrow.Direction.NORTH);
            arrowDown = new GuiButtonArrow(1, x, y + height / 2, GuiButtonArrow.Direction.SOUTH);
            arrowUp.yPosition -= arrowUp.height;
        }
        setEnableBackgroundDrawing(true);
        setText("0");
    }

    @Override
    public boolean getEnableBackgroundDrawing() {
        return false; // We want the offset, but not the drawing itself.
    }

    public void setPositiveOnly(boolean positiveOnly) {
        this.positiveOnly = positiveOnly;
    }

    public int getInt() throws NumberFormatException {
        return Integer.parseInt(getText());
    }

    public double getDouble() throws NumberFormatException {
        return Double.parseDouble(getText());
    }

    public float getFloat() throws NumberFormatException {
        return Float.parseFloat(getText());
    }

    @Override
    public void drawTextBox(Minecraft minecraft, int mouseX, int mouseY) {
        int offsetX = 0;
        if(arrows) {
            arrowUp.drawButton(minecraft, mouseX, mouseY);
            arrowDown.drawButton(minecraft, mouseX, mouseY);
            offsetX = arrowUp.width;
            xPosition += offsetX;
            width -= offsetX;
        }
        super.drawTextBox(minecraft, mouseX, mouseY);
        if(arrows) {
            xPosition -= offsetX;
            width += offsetX;
        }
    }

    protected int validateNumber(int number) {
        if(this.positiveOnly) {
            return Math.max(number, 0);
        } else {
            return number;
        }
    }

    protected int getDiffAmount() {
        return Minecraft.getMinecraft().thePlayer.isSneaking() ? 10 : 1;
    }

    protected void increase() {
        try {
            setText(Integer.toString(validateNumber(getInt() + getDiffAmount())));
        } catch (NumberFormatException e) {
            setText("0");
        }
    }

    protected void decrease() {
        try {
            setText(Integer.toString(validateNumber(getInt() - getDiffAmount())));
        } catch (NumberFormatException e) {
            setText("0");
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(arrowUp.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            increase();
        } else if(arrowDown.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            decrease();
        } else {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        arrowDown.enabled = true;
        try {
            if (getInt() == 0) {
                arrowDown.enabled = false;
            }
        } catch (NumberFormatException e ) {

        }
    }

}
