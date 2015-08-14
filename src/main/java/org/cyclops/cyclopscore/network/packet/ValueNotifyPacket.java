package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for sending a value from server to client.
 * @see org.cyclops.cyclopscore.inventory.IValueNotifier
 * @see IValueNotifiable
 * @author rubensworks
 *
 */
public class ValueNotifyPacket extends PacketCodec {

	@CodecField
	private int valueId;
	@CodecField
	private NBTTagCompound value;

    public ValueNotifyPacket() {

    }

    public ValueNotifyPacket(int valueId, NBTTagCompound value) {
		this.valueId = valueId;
		this.value = value;
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		if(player.openContainer instanceof IValueNotifiable) {
			((IValueNotifiable) player.openContainer).onUpdate(valueId, value);
		}
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {

	}
	
}