package org.cyclops.cyclopscore.client.gui.component.input;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.client.gui.image.Images;

/**
 * An extended text field.
 * @author rubensworks
 */
public class WidgetTextFieldExtended extends EditBox {

    private final boolean background;
    private IInputListener listener;

    public WidgetTextFieldExtended(Font fontrenderer, int x, int y, int width, int height,
                                   Component narrationMessage, boolean background) {
        super(fontrenderer, x, y, width, height, narrationMessage);
        this.background = background;
    }

    public WidgetTextFieldExtended(Font fontrenderer, int x, int y, int width, int height,
                                   Component narrationMessage) {
        this(fontrenderer, x, y, width, height, narrationMessage, false);
    }

    public void setListener(IInputListener listener) {
        this.listener = listener;
    }

    public int getInnerWidth() {
        return this.width - 7;
    }

    protected void drawBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        setX(getX() - 1);
        setY(getY() - 1);
        guiGraphics.blit(Images.WIDGETS, getX(), getY(), 0, 0, width / 2, height / 2);//top left
        guiGraphics.blit(Images.WIDGETS, getX() + width / 2, getY(), 200 - width / 2, 0, width / 2, height / 2);//top right
        guiGraphics.blit(Images.WIDGETS, getX(), getY() + height / 2, 0, 20 - height / 2, width / 2, height / 2);//bottom left
        guiGraphics.blit(Images.WIDGETS, getX() + width / 2, getY() + height / 2, 200 - width / 2, 20 - height / 2, width / 2, height / 2);//bottom right
        setX(getX() + 1);
        setY(getY() + 1);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if(listener != null) listener.onChanged();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(background) {
            drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 1 && mouseX >= this.getX() && mouseX < this.getX() + this.width
                && mouseY >= this.getY() && mouseY < this.getY() + this.height) {
            // Select everything
            this.setFocused(true);
            this.moveCursorTo(0);
            this.setHighlightPos(Integer.MAX_VALUE);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
