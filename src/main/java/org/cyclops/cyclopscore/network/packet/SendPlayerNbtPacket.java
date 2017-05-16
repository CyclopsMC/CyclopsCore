package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update persisted player NBT data.
 * @author rubensworks
 *
 */
public class SendPlayerNbtPacket extends PacketCodec {

	@CodecField
	private NBTTagCompound nbtData;

    public SendPlayerNbtPacket() {

    }

	public SendPlayerNbtPacket(EntityPlayer player) {
		this.nbtData = EntityHelpers.getPersistedPlayerNbt(player);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, nbtData);
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {

	}
	
}