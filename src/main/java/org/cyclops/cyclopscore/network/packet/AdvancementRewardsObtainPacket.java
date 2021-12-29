package org.cyclops.cyclopscore.network.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.infobook.pageelement.AdvancementRewards;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for obtaining advancement rewards.
 * @author rubensworks
 *
 */
public class AdvancementRewardsObtainPacket extends PacketCodec {

    @CodecField
    private String advancementRewardsId;

    public AdvancementRewardsObtainPacket() {

    }

    public AdvancementRewardsObtainPacket(String advancementRewardsId) {
        this.advancementRewardsId = advancementRewardsId;
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
        AdvancementRewards advancementRewards = AdvancementRewards.getAdvancementRewards(advancementRewardsId);
        if (advancementRewards != null) {
            advancementRewards.obtain(player);
        } else {
            CyclopsCore.clog(org.apache.logging.log4j.Level.WARN, String.format("Received an invalid advancement reward id '%s' from %s.", advancementRewardsId, player.getDisplayName().getString()));
        }
    }

}
