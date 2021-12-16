package org.cyclops.cyclopscore.client.gui.component.button;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.client.gui.image.Image;
import org.cyclops.cyclopscore.client.gui.image.Images;

/**
 * Inspired by {@link Checkbox}, but more flexible.
 * @author rubensworks
 */
public class ButtonCheckbox extends Button {
    private boolean checked;

    public ButtonCheckbox(int x, int y, int width, int height, Component title, OnPress pressedAction) {
        super(x, y, width, height, title, pressedAction);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void onPress() {
        setChecked(!isChecked());
        super.onPress();
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(visible) {
            // Determine image
            int i = 0;
            if (isChecked()) {
                i = 2;
            } else if (isHovered) {
                i = 1;
            }
            Image image = Images.CHECKBOX[i];

            // Determine position
            int imageWidth = image.getWidth();
            int imageWHeight = image.getHeight();
            int x = this.width <= imageWidth ? this.x : this.x + (this.width - imageWidth) / 2;
            int y = this.height <= imageWHeight ? this.y : this.y + (this.height - imageWHeight) / 2;

            // Draw image
            image.draw(this, matrixStack, x, y);
        }
    }
}
