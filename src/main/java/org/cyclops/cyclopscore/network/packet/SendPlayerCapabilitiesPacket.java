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

import javax.annotation.Nullable;

/**
 * Packet from server to client to update capabilities.
 * @author rubensworks
 * @deprecated This is a dirty hack, and should not be used, as it can break other mod's capabilities.
 */
@Deprecated
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
	public void actionClient(World world, @Nullable EntityPlayer player) {
    	if (player != null && capabilityData != null) {
			ObfuscationHelpers.getEntityCapabilities(player).deserializeNBT(capabilityData);
		}
	}

	@Override
	public void actionServer(World world, @Nullable EntityPlayerMP player) {

	}
	
}