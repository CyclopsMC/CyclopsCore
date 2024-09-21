package org.cyclops.cyclopscore.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Event hook for showing the ring of fire.
 * @author rubensworks
 *
 */
public class PlayerRingOfFireNeoForge extends PlayerRingOfFire {

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
