package org.cyclops.cyclopscore.network.packet;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to advancement unlocked info.
 * @author rubensworks
 *
 */
public class RequestPlayerAdvancementUnlockedPacket extends PacketCodec<RequestPlayerAdvancementUnlockedPacket> {

    public static final Type<RequestPlayerAdvancementUnlockedPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "request_player_advancement_unlocked_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestPlayerAdvancementUnlockedPacket> CODEC = getCodec(RequestPlayerAdvancementUnlockedPacket::new);

    @CodecField
    private String advancementId;

    public RequestPlayerAdvancementUnlockedPacket() {
        super(TYPE);
    }

    public RequestPlayerAdvancementUnlockedPacket(String advancementId) {
        this();
        this.advancementId = advancementId;
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
        AdvancementHolder advancement = AdvancementHelpers.getAdvancement(Dist.DEDICATED_SERVER, Identifier.parse(advancementId));
        if (advancement == null) {
            CyclopsCoreNeoForge.clog(org.apache.logging.log4j.Level.ERROR, "Received an invalid advancement " + advancementId + " from " + player.getName());
            return;
        }
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(
                new SendPlayerAdvancementUnlockedPacket(advancementId, AdvancementHelpers
                        .hasAdvancementUnlocked(player, advancement)), player);
    }

}
