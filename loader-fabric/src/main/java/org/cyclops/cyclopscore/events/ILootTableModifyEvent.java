package org.cyclops.cyclopscore.events;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * @author rubensworks
 */
public interface ILootTableModifyEvent {
    Event<ILootTableModifyEvent> EVENT = EventFactory.createArrayBacked(ILootTableModifyEvent.class,
            (listeners) -> (player, menu, itemsStacks) -> {
                for (ILootTableModifyEvent event : listeners) {
                    event.getLootTableItems(player, menu, itemsStacks);
                }
            }
    );

    void getLootTableItems(LootTable lootTable, LootContext context, ObjectArrayList<ItemStack> itemsStacks);
}
