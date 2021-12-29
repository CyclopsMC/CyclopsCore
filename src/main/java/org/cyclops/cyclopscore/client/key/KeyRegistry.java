package org.cyclops.cyclopscore.client.key;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Enum that contains all custom hotkeys that
 * are added. Every key also has a
 * {@link KeyMapping} for that specific key.
 *
 * @author immortaleeb
 *
 */
public class KeyRegistry implements IKeyRegistry {

    private final Multimap<KeyMapping, IKeyHandler> keyHandlerMap = HashMultimap.create();

    /**
     * Create a new keybinding.
     * @param mod The mod.
     * @param name The unique name.
     * @param defaultKey The keycode.
     * @return A new keybinding.
     */
    public static KeyMapping newKeyMapping(ModBase mod, String name, int defaultKey) {
        String id = L10NHelpers.localize("key." + mod.getModId() + "." + name);
        String category = L10NHelpers.localize("key.categories." + mod.getModId());
        return new KeyMapping(id, defaultKey, category);
    }

    @Override
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerKeyInput(InputEvent.KeyInputEvent event) {
        for (KeyMapping kb : keyHandlerMap.keySet()) {
            if (kb.isDown()) {
                fireKeyPressed(kb);
            }
        }
    }

    private void fireKeyPressed(KeyMapping kb) {
        for (IKeyHandler h : keyHandlerMap.get(kb)) {
            h.onKeyPressed(kb);
        }
    }

    @Override
    public void addKeyHandler(KeyMapping kb, IKeyHandler handler) {
        keyHandlerMap.put(kb, handler);
    }

}
