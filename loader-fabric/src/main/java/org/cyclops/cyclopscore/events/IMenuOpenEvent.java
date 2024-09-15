package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * @author rubensworks
 */
public interface IMenuOpenEvent {
    Event<IMenuOpenEvent> EVENT = EventFactory.createArrayBacked(IMenuOpenEvent.class,
            (listeners) -> (player, menu) -> {
                for (IMenuOpenEvent event : listeners) {
                    event.onMenuOpened(player, menu);
                }
            }
    );

    void onMenuOpened(ServerPlayer player, AbstractContainerMenu menu);
}
