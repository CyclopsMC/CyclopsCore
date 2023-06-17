package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
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

    public WidgetNumberField(Font fontrenderer, int x, int y, int width, int height, boolean arrows,
                             Component narrationMessage, boolean background) {
        super(fontrenderer, x, y, width, height, narrationMessage, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowUp = new ButtonArrow(x, y + height / 2, Component.translatable("gui.cyclopscore.up"), (button) -> this.increase(), ButtonArrow.Direction.NORTH);
            arrowDown = new ButtonArrow(x, y + height / 2, Component.translatable("gui.cyclopscore.down"), (button) -> this.decrease(), ButtonArrow.Direction.SOUTH);
            arrowUp.setY(arrowUp.getY() - arrowUp.getHeight());
        }
        setBordered(true);
        setValue("0");
    }

    @Override
    public void setEditable(boolean enabled) {
        arrowUp.active = enabled;
        arrowDown.active = enabled;
        isEnabled = enabled;
        super.setEditable(enabled);
    }

    @Override
    public boolean isBordered() {
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
        updateArrowsState();
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
        return validateNumber(Integer.parseInt(getValue()));
    }

    public double getDouble() throws NumberFormatException {
        return validateNumber(Double.parseDouble(getValue()));
    }

    public float getFloat() throws NumberFormatException {
        return validateNumber(Float.parseFloat(getValue()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int offsetX = 0;
        if(arrows) {
            arrowUp.render(guiGraphics, mouseX, mouseY, partialTicks);
            arrowDown.render(guiGraphics, mouseX, mouseY, partialTicks);
            offsetX = arrowUp.getWidth();
            setX(getX() + offsetX);
            width -= offsetX;
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
        if(arrows) {
            setX(getX() - offsetX);
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
            setValue(Integer.toString(validateNumber(getInt() + getDiffAmount())));
        } catch (NumberFormatException e) {
            setValue("0");
        }
        updateArrowsState();
    }

    protected void decrease() {
        try {
            setValue(Integer.toString(validateNumber(getInt() - getDiffAmount())));
        } catch (NumberFormatException e) {
            setValue("0");
        }
        updateArrowsState();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        updateArrowsState();
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        boolean ret = super.charTyped(typedChar, keyCode);
        updateArrowsState();
        return ret;
    }

    @Override
    public boolean keyPressed(int typedChar, int keyCode, int modifiers) {
        boolean ret = super.keyPressed(typedChar, keyCode, modifiers);
        updateArrowsState();
        return ret;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean ret = arrowUp.mouseClicked(mouseX, mouseY, mouseButton)
                || arrowDown.mouseClicked(mouseX, mouseY, mouseButton)
                || super.mouseClicked(mouseX, mouseY, mouseButton);
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
