package org.cyclops.cyclopscore.network.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
@Deprecated // TODO: rm in next major
public abstract class PlayerPositionPacket<T extends PlayerPositionPacket<T>> extends PacketCodec<T> {

    private static final int DEFAULT_RANGE = 3000;

    @CodecField
    protected String uuid;
    @CodecField
    protected Vec3 position = new Vec3(0, 0, 0);
    @CodecField
    private int range = DEFAULT_RANGE;

    /**
     * Creates a packet with no content
     */
    public PlayerPositionPacket(Type<T> type) {
        super(type);
    }

    public PlayerPositionPacket(Type<T> type, Player player) {
        this(type, player, DEFAULT_RANGE);
    }

    public PlayerPositionPacket(Type<T> type, Player player, int range) {
        super(type);
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
    public void actionClient(Level level, Player player) {
        if (uuid == null) {
            getModInstance().log(org.apache.logging.log4j.Level.WARN, "Got PlayerPositionPacket with empty uuid");
            return;
        }

        try {
            UUID uuid = UUID.fromString(this.uuid);
            if (player != null && !player.getUUID().equals(uuid)) {
                player = level.getPlayerByUUID(uuid);
            }

            if (player == null) {
                getModInstance().log(org.apache.logging.log4j.Level.WARN, "Received PlayerPositionPacket for player with uuid '" + uuid + "', but player doesn't exist");
            } else {
                performClientAction(level, player);
            }
        } catch (IllegalArgumentException e) {
            // Ignore invalid packets
        }
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        getModInstance().getPacketHandler().sendToAllAround(create(player, range),
                LocationHelpers.createTargetPointFromEntity(player, range));
    }

    protected abstract PlayerPositionPacket create(Player player, int range);

    protected abstract ModBase getModInstance();

    protected abstract void performClientAction(Level level, Player player);
}
