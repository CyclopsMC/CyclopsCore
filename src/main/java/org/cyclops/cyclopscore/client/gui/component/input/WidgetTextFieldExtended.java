package org.cyclops.cyclopscore.client.gui.component.input;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.cyclopscore.client.gui.image.Images;
import org.cyclops.cyclopscore.helper.RenderHelpers;

/**
 * An extended text field.
 * @author rubensworks
 */
public class WidgetTextFieldExtended extends TextFieldWidget {

    private final boolean background;
    private IInputListener listener;

    public WidgetTextFieldExtended(FontRenderer fontrenderer, int x, int y, int width, int height,
                                   ITextComponent narrationMessage, boolean background) {
        super(fontrenderer, x, y, width, height, narrationMessage);
        this.background = background;
    }

    public WidgetTextFieldExtended(FontRenderer fontrenderer, int x, int y, int width, int height,
                                   ITextComponent narrationMessage) {
        this(fontrenderer, x, y, width, height, narrationMessage, false);
    }

    public void setListener(IInputListener listener) {
        this.listener = listener;
    }

    public int getAdjustedWidth() {
        return this.width - 7;
    }

    protected void drawBackground(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderHelpers.bindTexture(Images.WIDGETS);
        GlStateManager.color4f(1, 1, 1, 1);

        x--;
        y--;
        blit(matrixStack, x, y, 0, 0, width / 2, height / 2);//top left
        blit(matrixStack, x + width / 2, y, 200 - width / 2, 0, width / 2, height / 2);//top right
        blit(matrixStack, x, y + height / 2, 0, 20 - height / 2, width / 2, height / 2);//bottom left
        blit(matrixStack, x + width / 2, y + height / 2, 200 - width / 2, 20 - height / 2, width / 2, height / 2);//bottom right
        x++;
        y++;
    }

    @Override
    public void setText(String value) {
        super.setText(value);
        if(listener != null) listener.onChanged();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(background) {
            drawBackground(matrixStack, mouseX, mouseY, partialTicks);
        }
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        GlStateManager.color4f(1, 1, 1, 1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 1 && mouseX >= this.x && mouseX < this.x + this.width
                && mouseY >= this.y && mouseY < this.y + this.height) {
            // Select everything
            this.setFocused(true);
            this.setCursorPosition(0);
            this.setSelectionPos(Integer.MAX_VALUE);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
