package org.cyclops.cyclopscore.client.gui.component.input;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonArrow;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * A number field which by default only accepts positive numbers.
 * @author rubensworks
 */
public class WidgetNumberField extends WidgetTextFieldExtended {

    private final boolean arrows;
    private ButtonArrow arrowUp;
    private ButtonArrow arrowDown;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private boolean isEnabled = true;

    public WidgetNumberField(FontRenderer fontrenderer, int x, int y, int width, int height, boolean arrows,
                             String narrationMessage, boolean background) {
        super(fontrenderer, x, y, width, height, narrationMessage, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowUp = new ButtonArrow(x, y + height / 2, "gui.cyclopscore.up", (button) -> this.increase(), ButtonArrow.Direction.NORTH);
            arrowDown = new ButtonArrow(x, y + height / 2, "gui.cyclopscore.down", (button) -> this.decrease(), ButtonArrow.Direction.SOUTH);
            arrowUp.y -= arrowUp.getHeight();
        }
        setEnableBackgroundDrawing(true);
        setText("0");
    }

    @Override
    public void setEnabled(boolean enabled) {
        arrowUp.active = enabled;
        arrowDown.active = enabled;
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

    public int getMinValue() {
        return minValue;
    }

    /**
     * @param minValue The minimal value (inclusive)
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue The maximal value (inclusive)
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getInt() throws NumberFormatException {
        return validateNumber(Integer.parseInt(getText()));
    }

    public double getDouble() throws NumberFormatException {
        return validateNumber(Double.parseDouble(getText()));
    }

    public float getFloat() throws NumberFormatException {
        return validateNumber(Float.parseFloat(getText()));
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        int offsetX = 0;
        GlStateManager.color4f(1, 1, 1, 1);
        if(arrows) {
            arrowUp.renderButton(mouseX, mouseY, partialTicks);
            arrowDown.renderButton(mouseX, mouseY, partialTicks);
            offsetX = arrowUp.getWidth();
            x += offsetX;
            width -= offsetX;
        }
        super.renderButton(mouseX, mouseY, partialTicks);
        if(arrows) {
            x -= offsetX;
            width += offsetX;
        }
    }

    public int validateNumber(int number) {
        return Math.max(this.minValue, Math.min(this.maxValue, number));
    }

    public double validateNumber(double number) {
        return Math.max(this.minValue, Math.min(this.maxValue, number));
    }

    public float validateNumber(float number) {
        return Math.max(this.minValue, Math.min(this.maxValue, number));
    }

    protected int getDiffAmount() {
        return MinecraftHelpers.isShifted() ? 10 : 1;
    }

    protected void increase() {
        try {
            setText(Integer.toString(validateNumber(getInt() + getDiffAmount())));
        } catch (NumberFormatException e) {
            setText("0");
        }
        updateArrowsState();
    }

    protected void decrease() {
        try {
            setText(Integer.toString(validateNumber(getInt() - getDiffAmount())));
        } catch (NumberFormatException e) {
            setText("0");
        }
        updateArrowsState();
    }

    @Override
    public void setText(String value) {
        super.setText(value);
        updateArrowsState();
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        boolean ret = super.charTyped(typedChar, keyCode);
        updateArrowsState();
        return ret;
    }

    protected void updateArrowsState() {
        if (this.arrows) {
            arrowDown.active = true;
            arrowUp.active = true;
            try {
                if (getInt() <= this.minValue) {
                    arrowDown.active = false;
                }
                if (getInt() >= this.maxValue) {
                    arrowUp.active = false;
                }
            } catch (NumberFormatException e) {

            }
        }
    }

}
