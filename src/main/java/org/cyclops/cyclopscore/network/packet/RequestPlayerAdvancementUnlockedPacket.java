package org.cyclops.cyclopscore.network.packet;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to advancement unlocked info.
 * @author rubensworks
 *
 */
public class RequestPlayerAdvancementUnlockedPacket extends PacketCodec {

	@CodecField
	private String advancementId;

    public RequestPlayerAdvancementUnlockedPacket() {

    }

	public RequestPlayerAdvancementUnlockedPacket(String advancementId) {
		this.advancementId = advancementId;
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
		Advancement advancement = AdvancementHelpers.getAdvancement(new ResourceLocation(advancementId));
		if (advancement == null) {
			CyclopsCore.clog(Level.ERROR, "Received an invalid advancement " + advancementId + " from " + player.getName());
		}
		CyclopsCore._instance.getPacketHandler().sendToPlayer(
				new SendPlayerAdvancementUnlockedPacket(advancementId, AdvancementHelpers
						.hasAdvancementUnlocked(player, advancement)), player);
	}
	
}