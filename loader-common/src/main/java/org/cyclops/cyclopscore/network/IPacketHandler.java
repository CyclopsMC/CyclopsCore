package org.cyclops.cyclopscore.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * @author rubensworks
 */
public interface IPacketHandler {

    public <P extends PacketBase> void register(Class<P> clazz, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec);

    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public void sendToServer(PacketBase packet);

    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public void sendToPlayer(PacketBase packet, ServerPlayer player);

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The point.
     */
    public void sendToAllAroundPoint(PacketBase packet, TargetPoint point);

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public void sendToDimension(PacketBase packet, ServerLevel dimension);

    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public void sendToAll(PacketBase packet);

    public static record TargetPoint(ServerLevel level, double x, double y, double z, double radius, @Nullable ServerPlayer excluded) {}

}
