package org.cyclops.cyclopscore.network.packet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
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
	private CompoundTag value;

    public ValueNotifyPacket() {

    }

    public ValueNotifyPacket(MenuType<?> containerType, int valueId, CompoundTag value) {
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
	public void actionClient(Level level, Player player) {
		if(player.containerMenu instanceof IValueNotifiable) {
			IValueNotifiable container = ((IValueNotifiable) player.containerMenu);
			if (isContainerValid(container)) {
				container.onUpdate(valueId, value);
			}
		}
	}

	@Override
	public void actionServer(Level level, ServerPlayer player) {
		if(player.containerMenu instanceof IValueNotifiable) {
			IValueNotifiable container = ((IValueNotifiable) player.containerMenu);
			if (isContainerValid(container)) {
				container.onUpdate(valueId, value);
			}
		}
	}
	
}