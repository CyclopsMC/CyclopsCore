package org.cyclops.cyclopscore.event;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Event hook for showing the ring of fire.
 * @author rubensworks
 *
 */
public class PlayerRingOfFireForge extends PlayerRingOfFire {

    /**
     * When a player loggedin event is received.
     * @param event The received event.
     */
    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        spawnRing(event.getEntity());
    }

    /**
     * When a player respawn event is received.
     * @param event The received event.
     */
    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        spawnRing(event.getEntity());
    }

}
