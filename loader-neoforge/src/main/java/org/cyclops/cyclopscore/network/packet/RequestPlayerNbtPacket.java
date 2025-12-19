package org.cyclops.cyclopscore.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to persisted player NBT data.
 * @author rubensworks
 *
 */
public class RequestPlayerNbtPacket extends PacketCodec<RequestPlayerNbtPacket> {

    public static final Type<RequestPlayerNbtPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "request_player_nbt"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestPlayerNbtPacket> CODEC = getCodec(RequestPlayerNbtPacket::new);

    public RequestPlayerNbtPacket() {
        super(TYPE);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(Level level, Player player) {

    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(new SendPlayerNbtPacket(player), player);
    }

}
