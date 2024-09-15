package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.cyclops.cyclopscore.RegistryEntriesCommon;
import org.cyclops.cyclopscore.events.IMenuOpenEvent;

/**
 * @author rubensworks
 */
public class GuiContainerOpenTriggerEventHooksFabric {

    public GuiContainerOpenTriggerEventHooksFabric() {
        IMenuOpenEvent.EVENT.register(this::onMenuOpen);
    }

    private void onMenuOpen(ServerPlayer player, AbstractContainerMenu menu) {
        RegistryEntriesCommon.CRITERION_TRIGGER_GUI_CONTAINER_OPEN.value().trigger(player,
                (i) -> i.test(player, player.containerMenu));
    }

}
