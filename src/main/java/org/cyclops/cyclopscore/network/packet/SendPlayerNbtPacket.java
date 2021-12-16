package org.cyclops.cyclopscore.network.packet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
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
	private CompoundTag nbtData;

    public SendPlayerNbtPacket() {

    }

	public SendPlayerNbtPacket(Player player) {
		this.nbtData = EntityHelpers.getPersistedPlayerNbt(player);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(Level level, Player player) {
		player.getPersistentData().put(Player.PERSISTED_NBT_TAG, nbtData);
	}

	@Override
	public void actionServer(Level level, ServerPlayer player) {

	}
	
}