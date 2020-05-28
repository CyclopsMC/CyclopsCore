package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
	private String containerType;
	@CodecField
	private int valueId;
	@CodecField
	private CompoundNBT value;

    public ValueNotifyPacket() {

    }

    public ValueNotifyPacket(ContainerType<?> containerType, int valueId, CompoundNBT value) {
    	this.containerType = containerType.getRegistryName().toString();
		this.valueId = valueId;
		this.value = value;
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	protected boolean isContainerValid(IValueNotifiable container) {
    	return container.getValueNotifiableType().getRegistryName().toString().equals(containerType);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {
		if(player.openContainer instanceof IValueNotifiable) {
			IValueNotifiable container = ((IValueNotifiable) player.openContainer);
			if (isContainerValid(container)) {
				container.onUpdate(valueId, value);
			}
		}
	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		if(player.openContainer instanceof IValueNotifiable) {
			IValueNotifiable container = ((IValueNotifiable) player.openContainer);
			if (isContainerValid(container)) {
				container.onUpdate(valueId, value);
			}
		}
	}
	
}