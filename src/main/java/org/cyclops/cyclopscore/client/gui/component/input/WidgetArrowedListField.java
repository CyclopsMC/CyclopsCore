package org.cyclops.cyclopscore.client.gui.component.input;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonArrow;

import java.util.List;

/**
 * A number field which by default only accepts positive numbers.
 * @param <E> The element type
 * @author rubensworks
 */
public class WidgetArrowedListField<E> extends WidgetTextFieldExtended {

    private final boolean arrows;
    private ButtonArrow arrowLeft;
    private ButtonArrow arrowRight;
    private List<E> elements;
    private int activeElement;
    private IInputListener listener;

    public WidgetArrowedListField(FontRenderer fontrenderer, int x, int y, int width, int height, boolean arrows,
                                  ITextComponent narrationMessage, boolean background, List<E> elements) {
        super(fontrenderer, x, y, width, height, narrationMessage, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowLeft  = new ButtonArrow(x, y - 1, new TranslationTextComponent("gui.cyclopscore.left"), (button) -> this.decrease(), ButtonArrow.Direction.WEST);
            arrowRight = new ButtonArrow(x + width, y - 1, new TranslationTextComponent("gui.cyclopscore.right"), (button) -> this.increase(), ButtonArrow.Direction.EAST);
            arrowRight.x -= arrowRight.getWidth();
        }
        setBordered(true);
        this.elements = elements;
        setActiveElement(0);
    }

    public void setListener(IInputListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isBordered() {
        return false; // We want the offset, but not the drawing itself.
    }

    public void setActiveElement(int index) {
        if(index >= elements.size()) {
            this.activeElement = -1;
            setValue("");
        } else {
            this.activeElement = index;
            setValue(activeElementToString(getActiveElement()));
        }
        if(listener != null) listener.onChanged();
    }

    public boolean setActiveElement(E element) {
        int index = this.elements.indexOf(element);
        if (index < 0) {
            return false;
        }
        setActiveElement(index);
        return true;
    }

    protected String activeElementToString(E element) {
        return element.toString();
    }

    public E getActiveElement() throws NumberFormatException {
        if(activeElement < 0 || activeElement >= elements.size()) {
            return null;
        }
        return elements.get(activeElement);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int offsetX = 0;
        if(arrows) {
            arrowLeft.renderButton(matrixStack, mouseX, mouseY, partialTicks);
            arrowRight.renderButton(matrixStack, mouseX, mouseY, partialTicks);
            offsetX = arrowLeft.getWidth();
            x += offsetX + 1;
            width -= offsetX * 2;
        }
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if(arrows) {
            x -= offsetX + 1;
            width += offsetX * 2;
        }
    }

    protected void increase() {
        setActiveElement((activeElement + 1) % elements.size());
    }

    protected void decrease() {
        setActiveElement((activeElement - 1 + elements.size()) % elements.size());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return arrowLeft.mouseClicked(mouseX, mouseY, mouseButton)
                || arrowRight.mouseClicked(mouseX, mouseY, mouseButton)
                || super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
