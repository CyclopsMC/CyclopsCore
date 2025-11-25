package org.cyclops.cyclopscore.client.key;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.proxy.IClientProxy;

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
    public static KeyMapping newKeyMapping(ModBaseNeoForge mod, String name, int defaultKey) {
        String id = IModHelpers.get().getL10NHelpers().localize("key." + mod.getModId() + "." + name);
        return new KeyMapping(id, defaultKey, ((IClientProxy) mod.getProxy()).getMainKeyCategory());
    }

    @Override
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerKeyInput(ClientTickEvent.Post event) {
        for (KeyMapping kb : keyHandlerMap.keySet()) {
            while (kb.consumeClick()) {
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
