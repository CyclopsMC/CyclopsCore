package org.cyclops.cyclopscore.client.gui.component.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.cyclopscore.client.gui.image.IImage;

/**
 * A button with an image.
 * @author rubensworks
 *
 */
public class ButtonImage extends ButtonExtended {

    private IImage[] images;
    private final int offsetX, offsetY;

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param narrationMessage The string to print.
	 * @param offsetX The x coordinate for the image inside the button.
	 * @param offsetY The y coordinate for the image inside the button.
	 * @param pressCallback A callback for when this button was pressed.
	 * @param images The images to render. First images are rendered behind later images.
	 * @param background If the button background should be rendered.
	 */
	public ButtonImage(int x, int y, int width, int height, ITextComponent narrationMessage,
					   Button.IPressable pressCallback, IImage[] images,
					   boolean background, int offsetX, int offsetY) {
		super(x, y, width, height, narrationMessage, pressCallback, background);
		this.images = images;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param narrationMessage The string to print.
	 * @param pressCallback A callback for when this button was pressed.
	 * @param images The images to render
	 */
	public ButtonImage(int x, int y, ITextComponent narrationMessage, Button.IPressable pressCallback, IImage... images) {
		this(x, y, images[0].getWidth(), images[0].getHeight(), narrationMessage, pressCallback, images, false, 0, 0);
	}

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param width Width
	 * @param height Height
	 * @param narrationMessage The string to print.
	 * @param pressCallback A callback for when this button was pressed.
	 * @param background If the button background should be rendered.
	 * @param image The image to render
     * @param offsetX The x coordinate for the image inside the button.
     * @param offsetY The y coordinate for the image inside the button.
	 */
	public ButtonImage(int x, int y, int width, int height,
					   ITextComponent narrationMessage, Button.IPressable pressCallback, boolean background,
					   IImage image, int offsetX, int offsetY) {
		this(x, y, width, height, narrationMessage, pressCallback, new IImage[]{image}, background, offsetX, offsetY);
	}

	/**
	 * Make a new instance.
	 * @param x X
	 * @param y Y
	 * @param narrationMessage The string to print.
	 * @param pressCallback A callback for when this button was pressed.
	 * @param image The image to render
	 */
	public ButtonImage(int x, int y, ITextComponent narrationMessage, Button.IPressable pressCallback, IImage image) {
		this(x, y, image.getWidth(), image.getHeight(), narrationMessage, pressCallback, false, image, 0, 0);
	}

	@Override
	protected void drawButtonInner(MatrixStack matrixStack, int mouseX, int mouseY) {
		for (IImage image : this.images) {
			image.draw(this, matrixStack, x + offsetX, y + offsetY);
		}
    }

	public void setImage(IImage image) {
		this.images[0] = image;
	}

	public void setImages(IImage[] images) {
		this.images = images;
	}
}
