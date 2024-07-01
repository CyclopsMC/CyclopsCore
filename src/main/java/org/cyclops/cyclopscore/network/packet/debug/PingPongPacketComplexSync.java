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
public class PingPongPacketComplexSync extends PingPongPacketComplexAsync<PingPongPacketComplexSync> {

    public static final Type<PingPongPacketComplexSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ping_pong_complex_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PingPongPacketComplexSync> CODEC = getCodec(PingPongPacketComplexSync::new);

    /**
     * Empty packet.
     */
    public PingPongPacketComplexSync() {
        super(TYPE);
    }

    public PingPongPacketComplexSync(int remaining, String string1, String string2) {
        super(TYPE, remaining, string1, string2);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketComplexSync(remaining - 1, string1, string2);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
