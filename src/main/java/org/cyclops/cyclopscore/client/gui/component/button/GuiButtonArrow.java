package org.cyclops.cyclopscore.client.gui.component.button;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.cyclops.cyclopscore.client.gui.image.Image;
import org.cyclops.cyclopscore.client.gui.image.Images;

/**
 * A button with an arrow in a certain direction.
 * @author rubensworks
 *
 */
public class GuiButtonArrow extends GuiButtonExtended {

    @Getter
    private final GuiButtonArrow.Direction direction;
    private final Image[] directionImages;

	/**
	 * Make a new instance.
	 * @param id The ID.
	 * @param x X
	 * @param y Y
     * @param direction The direction of the arrow to draw.
	 */
	public GuiButtonArrow(int id, int x, int y, GuiButtonArrow.Direction direction) {
		super(id, x, y, direction.width, direction.height, "", true);
        this.direction = direction;
        this.directionImages = getDirectionImage(direction);
	}

    protected static Image[] getDirectionImage(GuiButtonArrow.Direction direction) {
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
    protected void drawBackground(Minecraft minecraft, int hoverState) {
        directionImages[hoverState].draw(this, xPosition, yPosition);
    }

    @Override
    protected void drawButtonInner(Minecraft minecraft, int i, int j, boolean mouseOver) {

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
