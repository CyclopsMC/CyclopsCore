package org.cyclops.cyclopscore.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Event hook for showing the ring of fire.
 * @author rubensworks
 *
 */
public class PlayerRingOfFireFabric extends PlayerRingOfFire {

    public PlayerRingOfFireFabric() {
        ServerEntityEvents.ENTITY_LOAD.register(this::onLoad);
    }

    private void onLoad(Entity entity, ServerLevel level) {
        if (entity instanceof Player player) {
            spawnRing(player);
        }
    }

}
