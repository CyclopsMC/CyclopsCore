package org.cyclops.cyclopscore.network.packet;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to advancement unlocked info.
 * @author rubensworks
 *
 */
public class RequestPlayerAdvancementUnlockedPacket extends PacketCodec {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "request_player_advancement_unlocked_packet");

    @CodecField
    private String advancementId;

    public RequestPlayerAdvancementUnlockedPacket() {
        super(ID);
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
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {

    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        AdvancementHolder advancement = AdvancementHelpers.getAdvancement(Dist.DEDICATED_SERVER, new ResourceLocation(advancementId));
        if (advancement == null) {
            CyclopsCore.clog(org.apache.logging.log4j.Level.ERROR, "Received an invalid advancement " + advancementId + " from " + player.getName());
            return;
        }
        CyclopsCore._instance.getPacketHandler().sendToPlayer(
                new SendPlayerAdvancementUnlockedPacket(advancementId, AdvancementHelpers
                        .hasAdvancementUnlocked(player, advancement)), player);
    }

}
