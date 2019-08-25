package org.cyclops.cyclopscore.client.key;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Enum that contains all custom hotkeys that
 * are added. Every key also has a
 * {@link KeyBinding} for that specific key.
 * 
 * @author immortaleeb
 *
 */
public class KeyRegistry implements IKeyRegistry {

    private final Multimap<KeyBinding, IKeyHandler> keyHandlerMap = HashMultimap.create();

    /**
     * Create a new keybinding.
     * @param mod The mod.
     * @param name The unique name.
     * @param defaultKey The keycode.
     * @return A new keybinding.
     */
    public static KeyBinding newKeyBinding(ModBase mod, String name, int defaultKey) {
        String id = L10NHelpers.localize("key." + mod.getModId() + "." + name);
        String category = L10NHelpers.localize("key.categories." + mod.getModId());
        return new KeyBinding(id, defaultKey, category);
    }

	@Override
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerKeyInput(InputEvent.KeyInputEvent event) {
		for (KeyBinding kb : keyHandlerMap.keySet()) {
			if (kb.isPressed()) {
                fireKeyPressed(kb);
            }
		}
	}

	private void fireKeyPressed(KeyBinding kb) {
		for (IKeyHandler h : keyHandlerMap.get(kb)) {
			h.onKeyPressed(kb);
		}
	}

	@Override
	public void addKeyHandler(KeyBinding kb, IKeyHandler handler) {
        keyHandlerMap.put(kb, handler);
	}
	
}
