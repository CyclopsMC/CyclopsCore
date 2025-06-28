package org.cyclops.cyclopscore.event;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @author rubensworks
 */
public class LecternInfoBookHandlerForge {

    public static boolean onRightClickLectern(PlayerInteractEvent.RightClickBlock event) {
        if (LecternInfoBookHandler.onRightClickLectern(event.getLevel(), event.getPos(), event.getEntity())) {
            return true;
        }
        return false;
    }

}
