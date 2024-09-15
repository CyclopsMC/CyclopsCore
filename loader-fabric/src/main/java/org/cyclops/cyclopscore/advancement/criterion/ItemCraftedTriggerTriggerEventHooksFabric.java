package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.events.IItemCraftedEvent;

/**
 * @author rubensworks
 */
public class ItemCraftedTriggerTriggerEventHooksFabric {

    public ItemCraftedTriggerTriggerEventHooksFabric() {
        IItemCraftedEvent.EVENT.register(this::onCrafted);
    }

    public void onCrafted(ServerPlayer player, ItemStack itemStack) {
        RegistryEntriesCommon.CRITERION_TRIGGER_ITEM_CRAFTED.value().trigger(player,
                (i) -> i.test(player, itemStack));
    }

}
