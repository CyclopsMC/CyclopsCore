package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * @author rubensworks
 */
public class ModItemObtainedTriggerEventHooksNeoForge {

    public ModItemObtainedTriggerEventHooksNeoForge() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPickup(ItemEntityPickupEvent.Post event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger((ServerPlayer) event.getPlayer(),
                    (i) -> i.test((ServerPlayer) event.getPlayer(), event.getOriginalStack()));
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getCrafting()));
        }
    }

    @SubscribeEvent
    public void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getSmelting()));
        }
    }

}
