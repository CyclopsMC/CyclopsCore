package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

/**
 * @author rubensworks
 */
public interface IPlayerLoggedInEvent {
    Event<IPlayerLoggedInEvent> EVENT = EventFactory.createArrayBacked(IPlayerLoggedInEvent.class,
            (listeners) -> (player) -> {
                for (IPlayerLoggedInEvent event : listeners) {
                    event.onPlayerLoggedIn(player);
                }
            }
    );

    void onPlayerLoggedIn(ServerPlayer player);
}
