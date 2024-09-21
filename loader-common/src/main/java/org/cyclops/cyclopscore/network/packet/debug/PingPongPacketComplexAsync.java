package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.network.CodecField;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketComplexAsync<T extends PingPongPacketComplexAsync<T>> extends PingPongPacketAsync<T> {

    public static final Type<PingPongPacketComplexAsync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ping_pong_complex_async"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PingPongPacketComplexAsync> CODEC = getCodec(PingPongPacketComplexAsync::new);

    @CodecField
    protected String string1;
    @CodecField
    protected String string2;

    /**
     * Empty packet.
     */
    public PingPongPacketComplexAsync() {
        super((Type) TYPE);
    }

    public PingPongPacketComplexAsync(int remaining, String string1, String string2) {
        super((Type) TYPE, remaining);
        this.string1 = string1;
        this.string2 = string2;
    }

    public PingPongPacketComplexAsync(Type<T> type) {
        super(type);
    }

    public PingPongPacketComplexAsync(Type<T> type, int remaining, String string1, String string2) {
        super(type, remaining);
        this.string1 = string1;
        this.string2 = string2;
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketComplexAsync<>(remaining - 1, string1, string2);
    }

    @Override
    protected void log(Player player, String message) {
        super.log(player, message);
        super.log(player, String.format("[EXTRA] '%s' and '%s' ", string1, string2));
    }
}
