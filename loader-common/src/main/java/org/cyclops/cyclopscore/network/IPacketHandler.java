package org.cyclops.cyclopscore.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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

    /**
     * Creates a {@link IPacketHandler.TargetPoint} for the dimension and position of the given {@link Entity}
     * and a given range.
     *
     * @param entity Entity who's dimension and position will be used to create the {@link IPacketHandler.TargetPoint}.
     * @param range The range of the {@link IPacketHandler.TargetPoint}.
     * @return A {@link IPacketHandler.TargetPoint} with the position and dimension of the entity and the given range.
     */
    public static IPacketHandler.TargetPoint createTargetPointFromEntity(Entity entity, int range) {
        return new IPacketHandler.TargetPoint((ServerLevel) entity.level(), entity.getX(), entity.getY(), entity.getZ(), range, null);
    }

    /**
     * Creates a {@link IPacketHandler.TargetPoint} for the dimension of the given world and the
     * given {@link BlockPos}.
     *
     * @param world The world from which the dimension will be used.
     * @param location The location for the target.
     * @param range The range of the {@link IPacketHandler.TargetPoint}.
     * @return A {@link IPacketHandler.TargetPoint} with the position and dimension of the entity and the given range.
     */
    public static IPacketHandler.TargetPoint createTargetPointFromLocation(ServerLevel world, BlockPos location, int range) {
        return new IPacketHandler.TargetPoint(world, location.getX(), location.getY(), location.getZ(), range, null);
    }

}
