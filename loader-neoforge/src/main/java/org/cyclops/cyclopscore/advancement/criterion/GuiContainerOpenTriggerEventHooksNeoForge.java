package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * @author rubensworks
 */
public class GuiContainerOpenTriggerEventHooksNeoForge {

    public GuiContainerOpenTriggerEventHooksNeoForge() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEvent(PlayerContainerEvent.Open event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_GUI_CONTAINER_OPEN.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getContainer()));
        }
    }

}
