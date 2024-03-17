package org.cyclops.cyclopscore.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * The base packet for packets.
 * All packets must have a default constructor.
 * @author rubensworks
 *
 */
public abstract class PacketBase implements CustomPacketPayload {

    private final ResourceLocation id;

    protected PacketBase(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        try {
            this.encode(buf);
        } catch (Throwable e) {
            throw new PacketHandler.PacketCodecException("An exception occurred during encoding of packet " + this.toString(), e);
        }
    }

    @Override
    public ResourceLocation id() {
        return this.id;
    }

    /**
     * @return If this packet can run on a thread other than the main-thread of Minecraft.
     *         If this is asynchronous, the player parameter inside the action is not guaranteed to be defined.
     */
    public abstract boolean isAsync();

    /**
     * Encode this packet.
     * @param output The byte array to encode to.
     */
    public abstract void encode(FriendlyByteBuf output);

    /**
     * Decode for this packet.
     * @param input The byte array to decode from.
     */
    public abstract void decode(FriendlyByteBuf input);

    /**
     * Actions for client-side.
     * @param level The world.
     * @param player The player. Can be null if this packet is asynchronous.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract void actionClient(Level level, Player player);

    /**
     * Actions for server-side.
     * @param level The world.
     * @param player The player.
     */
    public abstract void actionServer(Level level, ServerPlayer player);

}
