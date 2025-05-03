package org.cyclops.cyclopscore.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandlerNeoForge {

    @SubscribeEvent
    public void onRightClickLectern(PlayerInteractEvent.RightClickBlock event) {
        if (LecternInfoBookHandler.onRightClickLectern(event.getLevel(), event.getPos(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

}
