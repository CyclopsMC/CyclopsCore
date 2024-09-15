package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.events.IItemCraftedEvent;
import org.cyclops.cyclopscore.events.IItemPickupEvent;
import org.cyclops.cyclopscore.events.IItemSmeltedEvent;

/**
 * @author rubensworks
 */
public class ModItemObtainedTriggerEventHooksFabric {

    public ModItemObtainedTriggerEventHooksFabric() {
        IItemPickupEvent.EVENT.register(this::onPickup);
        IItemCraftedEvent.EVENT.register(this::onCrafted);
        IItemSmeltedEvent.EVENT.register(this::onSmelted);
    }

    public void onPickup(Player player, ItemEntity itemEntity) {
        if (player instanceof ServerPlayer serverPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger(serverPlayer,
                    (i) -> i.test(serverPlayer, itemEntity.getItem()));
        }
    }

    public void onCrafted(ServerPlayer player, ItemStack itemStack) {
        RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger(player,
                (i) -> i.test(player, itemStack));
    }

    public void onSmelted(ServerPlayer player, ItemStack itemStack) {
        RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger(player,
                (i) -> i.test(player, itemStack));
    }

}
