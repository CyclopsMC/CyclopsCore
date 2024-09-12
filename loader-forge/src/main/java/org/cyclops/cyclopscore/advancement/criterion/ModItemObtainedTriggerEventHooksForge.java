package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * @author rubensworks
 */
public class ModItemObtainedTriggerEventHooksForge {

    public ModItemObtainedTriggerEventHooksForge() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_MOD_ITEM_OBTAINED.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getStack()));
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
