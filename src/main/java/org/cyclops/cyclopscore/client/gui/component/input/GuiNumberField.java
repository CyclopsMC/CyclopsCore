package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.cyclops.cyclopscore.client.gui.component.button.GuiButtonArrow;

/**
 * A number field which by default only accepts positive numbers.
 * @author rubensworks
 */
public class GuiNumberField extends GuiTextFieldExtended {

    private final boolean arrows;
    private GuiButtonArrow arrowUp;
    private GuiButtonArrow arrowDown;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private boolean isEnabled = true;

    public GuiNumberField(int componentId, FontRenderer fontrenderer, int x, int y, int width, int height, boolean arrows, boolean background) {
        super(componentId, fontrenderer, x, y, width, height, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowUp = new GuiButtonArrow(0, x, y + height / 2, GuiButtonArrow.Direction.NORTH);
            arrowDown = new GuiButtonArrow(1, x, y + height / 2, GuiButtonArrow.Direction.SOUTH);
            arrowUp.y -= arrowUp.height;
        }
        setEnableBackgroundDrawing(true);
        setText("0");
    }

    @Override
    public void setEnabled(boolean enabled) {
        arrowUp.enabled = enabled;
        arrowDown.enabled = enabled;
        isEnabled = enabled;
        super.setEnabled(enabled);
    }

    @Override
    public boolean getEnableBackgroundDrawing() {
        return false; // We want the offset, but not the drawing itself.
    }

    public void setPositiveOnly(boolean positiveOnly) {
        setMinValue(positiveOnly ? 0 : Integer.MIN_VALUE);
    }

    /**
     * @param minValue The minimal value (inclusive)
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * @param maxValue The maximal value (inclusive)
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
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
        GlStateManager.color(1, 1, 1, 1);
        if(arrows) {
            arrowUp.drawButton(minecraft, mouseX, mouseY, minecraft.getRenderPartialTicks());
            arrowDown.drawButton(minecraft, mouseX, mouseY, minecraft.getRenderPartialTicks());
            offsetX = arrowUp.width;
            x += offsetX;
            width -= offsetX;
        }
        super.drawTextBox(minecraft, mouseX, mouseY);
        if(arrows) {
            x -= offsetX;
            width += offsetX;
        }
    }

    protected int validateNumber(int number) {
        return Math.max(this.minValue, Math.min(this.maxValue, number));
    }

    protected int getDiffAmount() {
        return Minecraft.getMinecraft().player.isSneaking() ? 10 : 1;
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
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isEnabled) {
            boolean ret = true;
            if (this.arrows && arrowUp.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
                increase();
            } else if (this.arrows && arrowDown.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
                decrease();
            } else {
                ret = super.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if (this.arrows) {
                arrowDown.enabled = true;
                arrowUp.enabled = true;
                try {
                    if (getInt() <= this.minValue) {
                        arrowDown.enabled = false;
                    }
                    if (getInt() >= this.maxValue) {
                        arrowUp.enabled = false;
                    }
                } catch (NumberFormatException e) {

                }
            }
            return ret;
        }
        return false;
    }

}
