package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.cyclops.cyclopscore.client.gui.component.button.GuiButtonArrow;

import java.util.List;

/**
 * A number field which by default only accepts positive numbers.
 * @param <E> The element type
 * @author rubensworks
 */
public class GuiArrowedListField<E> extends GuiTextFieldExtended {

    private final boolean arrows;
    private GuiButtonArrow arrowLeft;
    private GuiButtonArrow arrowRight;
    private List<E> elements;
    private int activeElement;
    private IInputListener listener;

    public GuiArrowedListField(int componentId, FontRenderer fontrenderer, int x, int y, int width, int height, boolean arrows, boolean background, List<E> elements) {
        super(componentId, fontrenderer, x, y, width, height, background);
        this.arrows = arrows;

        if(this.arrows) {
            arrowLeft  = new GuiButtonArrow(0, x, y, GuiButtonArrow.Direction.WEST);
            arrowRight = new GuiButtonArrow(1, x + width, y, GuiButtonArrow.Direction.EAST);
            arrowRight.x -= arrowRight.width;
        }
        setEnableBackgroundDrawing(true);
        this.elements = elements;
        setActiveElement(0);
    }

    public void setListener(IInputListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean getEnableBackgroundDrawing() {
        return false; // We want the offset, but not the drawing itself.
    }

    public void setActiveElement(int index) {
        if(index >= elements.size()) {
            this.activeElement = -1;
            setText("");
        } else {
            this.activeElement = index;
            setText(activeElementToString(getActiveElement()));
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
    public void drawTextBox(Minecraft minecraft, int mouseX, int mouseY) {
        int offsetX = 0;
        if(arrows) {
            arrowLeft.drawButton(minecraft, mouseX, mouseY, minecraft.getRenderPartialTicks());
            arrowRight.drawButton(minecraft, mouseX, mouseY, minecraft.getRenderPartialTicks());
            offsetX = arrowLeft.width;
            x += offsetX;
            width -= offsetX * 2;
        }
        super.drawTextBox(minecraft, mouseX, mouseY);
        if(arrows) {
            x -= offsetX;
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
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(arrowRight.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            increase();
            return true;
        } else if(arrowLeft.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            decrease();
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

}
