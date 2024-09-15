package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public interface IItemSmeltedEvent {
    Event<IItemSmeltedEvent> EVENT = EventFactory.createArrayBacked(IItemSmeltedEvent.class,
            (listeners) -> (player, entity) -> {
                for (IItemSmeltedEvent event : listeners) {
                    event.onCrafted(player, entity);
                }
            }
    );

    void onCrafted(ServerPlayer player, ItemStack itemStack);
}
