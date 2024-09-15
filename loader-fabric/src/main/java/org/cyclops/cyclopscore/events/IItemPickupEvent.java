package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author rubensworks
 */
public interface IItemPickupEvent {
    Event<IItemPickupEvent> EVENT = EventFactory.createArrayBacked(IItemPickupEvent.class,
            (listeners) -> (player, entity) -> {
                for (IItemPickupEvent event : listeners) {
                    event.onPickup(player, entity);
                }
            }
    );

    void onPickup(Player player, ItemEntity itemEntity);
}
