package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * @author rubensworks
 */
public class GuiContainerOpenTriggerEventHooksForge {

    public GuiContainerOpenTriggerEventHooksForge() {
        PlayerContainerEvent.Open.BUS.addListener(this::onEvent);
    }

    public void onEvent(PlayerContainerEvent.Open event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            RegistryEntriesCommon.CRITERION_TRIGGER_GUI_CONTAINER_OPEN.value().trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getContainer()));
        }
    }

}
