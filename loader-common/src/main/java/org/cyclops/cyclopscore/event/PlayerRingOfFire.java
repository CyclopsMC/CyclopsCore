package org.cyclops.cyclopscore.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Event hook for showing the ring of fire.
 * @author rubensworks
 *
 */
public abstract class PlayerRingOfFire {

    // List of players that have a ring of fire
    public static final Set<UUID> ALLOW_RING = new HashSet<>();
    static {
        ALLOW_RING.add(UUID.fromString("068d4de0-3a75-4c6a-9f01-8c37e16a394c")); // kroeserr
        ALLOW_RING.add(UUID.fromString("e1dc75c6-dcf9-4e0c-8fbf-9c6e5e44527c")); // _EeB_
        ALLOW_RING.add(UUID.fromString("3e13f558-fb72-4949-a842-07879924bc49")); // JonaBrackenwood
        ALLOW_RING.add(UUID.fromString("777e7aa3-9373-4511-8d75-f99d23ebe252")); // Davivs69
        ALLOW_RING.add(UUID.fromString("94b8bfe7-9102-405c-ab80-2c4468e918f9")); // JokerReaper
    }

    protected void spawnRing(Player player) {
        if(!player.level().isClientSide() && player.getGameProfile() != null
                && ALLOW_RING.contains(player.getGameProfile().getId())) {
            CyclopsCoreInstance.MOD.getPacketHandlerCommon().sendToAllAroundPoint(new RingOfFirePacket(player),
                    IPacketHandler.createTargetPointFromLocation((ServerLevel) player.level(), player.blockPosition(), 50));
        }
    }

}
