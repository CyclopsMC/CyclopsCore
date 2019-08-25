package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;
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
	public void actionClient(World world, PlayerEntity player) {
		
	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		AdvancementRewards advancementRewards = AdvancementRewards.getAdvancementRewards(advancementRewardsId);
		if (advancementRewards != null) {
			advancementRewards.obtain(player);
		} else {
			CyclopsCore.clog(Level.WARN, String.format("Received an invalid advancement reward id '%s' from %s.", advancementRewardsId, player.getDisplayName().getString()));
		}
	}
	
}