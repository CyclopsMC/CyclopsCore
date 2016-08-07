package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update capabilities.
 * @author rubensworks
 *
 */
public class SendPlayerCapabilitiesPacket extends PacketCodec {

	@CodecField
	private NBTTagCompound capabilityData;

    public SendPlayerCapabilitiesPacket() {

    }

	public SendPlayerCapabilitiesPacket(CapabilityDispatcher capabilityDispatcher) {
		this.capabilityData = capabilityDispatcher.serializeNBT();
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		ObfuscationHelpers.getEntityCapabilities(player).deserializeNBT(capabilityData);
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {

	}
	
}