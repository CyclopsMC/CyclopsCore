package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update player advancement unlocked info.
 * @author rubensworks
 *
 */
public class SendPlayerAdvancementUnlockedPacket extends PacketCodec {

	@CodecField
	private String advancementId;
	@CodecField
	private boolean unlocked;

    public SendPlayerAdvancementUnlockedPacket() {

    }

	public SendPlayerAdvancementUnlockedPacket(String advancementId, boolean unlocked) {
		this.advancementId = advancementId;
		this.unlocked = unlocked;
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		ResourceLocation id = new ResourceLocation(advancementId);
		if (unlocked) {
			AdvancementHelpers.ACHIEVED_ADVANCEMENTS.add(id);
		} else {
			AdvancementHelpers.ACHIEVED_ADVANCEMENTS.remove(id);
		}
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {

	}
	
}