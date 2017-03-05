package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.infobook.pageelement.AchievementRewards;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for obtaining achievement rewards.
 * @author rubensworks
 *
 */
public class AchievementRewardsObtainPacket extends PacketCodec {

    @CodecField
    private String achievementRewardsId;

    public AchievementRewardsObtainPacket() {

    }

    public AchievementRewardsObtainPacket(String achievementRewardsId) {
		this.achievementRewardsId = achievementRewardsId;
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		AchievementRewards achievementRewards = AchievementRewards.getAchievementRewards(achievementRewardsId);
		if (achievementRewards != null) {
			achievementRewards.obtain(player);
		} else {
			CyclopsCore.clog(Level.WARN, String.format("Received an invalid achievement reward id '%s' from %s.", achievementRewardsId, player.getDisplayNameString()));
		}
	}
	
}