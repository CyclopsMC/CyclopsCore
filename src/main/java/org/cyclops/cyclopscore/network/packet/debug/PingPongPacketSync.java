package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.Reference;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketSync extends PingPongPacketAsync<PingPongPacketSync> {

    public static final Type<PingPongPacketSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ping_pong_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PingPongPacketSync> CODEC = getCodec(PingPongPacketSync::new);

    /**
     * Empty packet.
     */
    public PingPongPacketSync() {
        super(TYPE);
    }

    public PingPongPacketSync(int remaining) {
        super(TYPE, remaining);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketSync(remaining - 1);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

}
