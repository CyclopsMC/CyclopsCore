package org.cyclops.cyclopscore.client.gui.component.button;

import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.client.gui.image.Image;
import org.cyclops.cyclopscore.client.gui.image.Images;

/**
 * A button with an arrow in a certain direction.
 * @author rubensworks
 *
 */
public class ButtonArrow extends ButtonExtended {

    @Getter
    private final ButtonArrow.Direction direction;
    private final Image[] directionImages;

    /**
     * Make a new instance.
     * @param x X
     * @param y Y
     * @param narrationMessage The string to print.
     * @param pressCallback A callback for when this button was pressed.
     * @param direction The direction of the arrow to draw.
     */
    public ButtonArrow(int x, int y, Component narrationMessage, Button.OnPress pressCallback, ButtonArrow.Direction direction) {
        super(x, y, direction.width, direction.height, narrationMessage, pressCallback, true);
        this.direction = direction;
        this.directionImages = getDirectionImage(direction);
    }

    protected static Image[] getDirectionImage(ButtonArrow.Direction direction) {
        if(direction == Direction.NORTH) {
            return Images.BUTTON_ARROW_UP;
        } else if(direction == Direction.EAST) {
            return Images.BUTTON_ARROW_RIGHT;
        } else if(direction == Direction.SOUTH) {
            return Images.BUTTON_ARROW_DOWN;
        } else if(direction == Direction.WEST) {
            return Images.BUTTON_ARROW_LEFT;
        }
        return null;
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics) {
        directionImages[getYImage()].draw(guiGraphics, getX(), getY());
    }

    @Override
    protected void drawButtonInner(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    public enum Direction {

        NORTH(15, 10),
        EAST(10, 15),
        SOUTH(15, 10),
        WEST(10, 15);

        private final int width, height;

        private Direction(int width, int height) {
            this.width = width;
            this.height = height;
        }

    }

}
