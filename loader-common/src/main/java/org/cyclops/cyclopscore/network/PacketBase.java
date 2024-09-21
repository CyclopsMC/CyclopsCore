package org.cyclops.cyclopscore.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * The base packet for packets.
 * All packets must have a default constructor.
 * @author rubensworks
 *
 */
public abstract class PacketBase<T extends PacketBase<T>> implements CustomPacketPayload {

    private final Type<T> type;

    protected PacketBase(Type<T> type) {
        this.type = type;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return this.type;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        try {
            this.encode(buf);
        } catch (Throwable e) {
            throw new PacketCodecException("An exception occurred during encoding of packet " + this.toString(), e);
        }
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
    public abstract void encode(RegistryFriendlyByteBuf output);

    /**
     * Decode for this packet.
     * @param input The byte array to decode from.
     */
    public abstract void decode(RegistryFriendlyByteBuf input);

    /**
     * Actions for client-side.
     * @param level The world.
     * @param player The player. Can be null if this packet is asynchronous.
     */
    public abstract void actionClient(Level level, Player player);

    /**
     * Actions for server-side.
     * @param level The world.
     * @param player The player.
     */
    public abstract void actionServer(Level level, ServerPlayer player);

    public static <T extends PacketBase<T>> StreamCodec<RegistryFriendlyByteBuf, T> getCodec(Supplier<T> packetFactory) {
        return StreamCodec.ofMember(
                T::write,
                buff -> {
                    T payload = packetFactory.get();
                    try {
                        payload.decode(buff);
                    } catch (Throwable e) {
                        throw new PacketCodecException("An exception occurred during decoding of packet " + payload.toString(), e);
                    }
                    return payload;
                }
        );
    }

}
