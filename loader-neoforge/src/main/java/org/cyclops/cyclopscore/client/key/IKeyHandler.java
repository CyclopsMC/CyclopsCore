package org.cyclops.cyclopscore.client.key;

import net.minecraft.client.KeyMapping;

/**
 * A handler responsible for handling key presses.
 *
 * @author immortaleeb
 *
 */
public interface IKeyHandler {

    /**
     * This method is called whenever a key, which is mapped
     * to this KeyHandler is pressed.
     *
     * @param kb {@link KeyMapping} of the key that was pressed.
     */
    public void onKeyPressed(KeyMapping kb);
}
