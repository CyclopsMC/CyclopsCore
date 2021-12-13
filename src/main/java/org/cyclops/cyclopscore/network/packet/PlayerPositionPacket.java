package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.util.UUID;

/**
 * An abstract superclass for all packets that contain the position and uuid
 * of a player and execute a specific action on the client side that is visible by
 * all players in a specific range around the given player.
 *
 * @author immortaleeb
 */
public abstract class PlayerPositionPacket extends PacketCodec {

    private static final int DEFAULT_RANGE = 3000;

    @CodecField
    protected String uuid;
    @CodecField
    protected Vector3d position = new Vector3d(0, 0, 0);
    @CodecField
    private int range = DEFAULT_RANGE;

    /**
     * Creates a packet with no content
     */
    public PlayerPositionPacket() {
    }

    /**
     * Creates a PlayerPositionPacket which contains the player data.
     * @param player The player data.
     */
    public PlayerPositionPacket(PlayerEntity player) {
        this(player, DEFAULT_RANGE);
    }

    /**
     * Creates a PlayerPositionPacket which contains the player data
     * and will be sent to all players in the specified range of the
     * player.
     * @param player The player data.
     * @param range The range around the player.
     */
    public PlayerPositionPacket(PlayerEntity player, int range) {
        this.uuid = player.getUUID().toString();
        this.position = player.position();
        this.range = range;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(World world, PlayerEntity player) {
        if (uuid == null) {
            getModInstance().log(Level.WARN, "Got PlayerPositionPacket with empty uuid");
            return;
        }

        try {
            UUID uuid = UUID.fromString(this.uuid);
            if (player != null && !player.getUUID().equals(uuid)) {
                player = world.getPlayerByUUID(uuid);
            }

            if (player == null) {
                getModInstance().log(Level.WARN, "Received PlayerPositionPacket for player with uuid '" + uuid + "', but player doesn't exist");
            } else {
                performClientAction(world, player);
            }
        } catch (IllegalArgumentException e) {
            // Ignore invalid packets
        }
    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {
        getModInstance().getPacketHandler().sendToAllAround(create(player, range),
                LocationHelpers.createTargetPointFromEntity(player, range));
    }

    protected abstract PlayerPositionPacket create(PlayerEntity player, int range);

    protected abstract ModBase getModInstance();

    protected abstract void performClientAction(World world, PlayerEntity player);
}
