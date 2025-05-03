package org.cyclops.cyclopscore.event;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandlerForge {

    @SubscribeEvent
    public void onRightClickLectern(PlayerInteractEvent.RightClickBlock event) {
        if (LecternInfoBookHandler.onRightClickLectern(event.getLevel(), event.getPos(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

}
