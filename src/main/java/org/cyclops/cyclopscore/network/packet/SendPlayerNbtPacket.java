package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
	private CompoundNBT nbtData;

    public SendPlayerNbtPacket() {

    }

	public SendPlayerNbtPacket(PlayerEntity player) {
		this.nbtData = EntityHelpers.getPersistedPlayerNbt(player);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {
		player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, nbtData);
	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {

	}
	
}