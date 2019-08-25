package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to persisted player NBT data.
 * @author rubensworks
 *
 */
public class RequestPlayerNbtPacket extends PacketCodec {

    public RequestPlayerNbtPacket() {

    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {

	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		CyclopsCore._instance.getPacketHandler().sendToPlayer(new SendPlayerNbtPacket(player), player);
	}
	
}