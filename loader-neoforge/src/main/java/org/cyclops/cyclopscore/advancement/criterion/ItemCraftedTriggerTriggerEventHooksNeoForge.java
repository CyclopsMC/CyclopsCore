package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * @author rubensworks
 */
public class ItemCraftedTriggerTriggerEventHooksNeoForge {

    public ItemCraftedTriggerTriggerEventHooksNeoForge() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_ITEM_CRAFTED.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getCrafting()));
        }
    }

}
