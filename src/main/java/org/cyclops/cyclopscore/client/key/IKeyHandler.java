package org.cyclops.cyclopscore.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A handler responsible for handling key presses.
 * 
 * @author immortaleeb
 *
 */
@OnlyIn(Dist.CLIENT)
public interface IKeyHandler {

	/**
	 * This method is called whenever a key, which is mapped
	 * to this KeyHandler is pressed.
	 * 
	 * @param kb {@link KeyBinding} of the key that was pressed.
	 */
	public void onKeyPressed(KeyMapping kb);
}
