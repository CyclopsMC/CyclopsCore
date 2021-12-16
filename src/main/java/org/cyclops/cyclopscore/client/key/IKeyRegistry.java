package org.cyclops.cyclopscore.client.key;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.init.IRegistry;

/**
 * Hotkey registry
 * @author rubensworks
 */
public interface IKeyRegistry extends IRegistry {

    /**
     * Handles key presses for keybindings added by EvilCraft
     * Make sure to annotate this with {@link SubscribeEvent}
     * @param event The input event.
     */
    public void onPlayerKeyInput(InputEvent.KeyInputEvent event);

    /**
     * Binds a {@link IKeyHandler} to key presses of the
     * specified {@link KeyMapping}. Whenever the {@link KeyMapping}
     * is pressed, {@link IKeyHandler#onKeyPressed(KeyMapping)}} is called.
     *
     * @param kb {@link KeyMapping} to which we bind the {@link IKeyHandler}.
     * @param handler {@link IKeyHandler} that will be linked to presses of the given {@link KeyMapping}.
     */
    public void addKeyHandler(KeyMapping kb, IKeyHandler handler);

}
