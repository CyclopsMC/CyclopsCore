package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public interface IItemCraftedEvent {
    Event<IItemCraftedEvent> EVENT = EventFactory.createArrayBacked(IItemCraftedEvent.class,
            (listeners) -> (player, entity) -> {
                for (IItemCraftedEvent event : listeners) {
                    event.onCrafted(player, entity);
                }
            }
    );

    void onCrafted(ServerPlayer player, ItemStack itemStack);
}
