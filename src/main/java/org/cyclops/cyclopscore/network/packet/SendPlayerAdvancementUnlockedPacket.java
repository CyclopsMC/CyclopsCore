package org.cyclops.cyclopscore.network.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update player advancement unlocked info.
 * @author rubensworks
 *
 */
public class SendPlayerAdvancementUnlockedPacket extends PacketCodec {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "send_player_advancement_unlocked");

    @CodecField
    private String advancementId;
    @CodecField
    private boolean unlocked;

    public SendPlayerAdvancementUnlockedPacket() {
        super(ID);
    }

    public SendPlayerAdvancementUnlockedPacket(String advancementId, boolean unlocked) {
        this();
        this.advancementId = advancementId;
        this.unlocked = unlocked;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        ResourceLocation id = new ResourceLocation(advancementId);
        if (unlocked) {
            AdvancementHelpers.ACHIEVED_ADVANCEMENTS.add(id);
        } else {
            AdvancementHelpers.ACHIEVED_ADVANCEMENTS.remove(id);
        }
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }

}
