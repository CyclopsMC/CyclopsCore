package org.cyclops.cyclopscore.network.packet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
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
	public void actionClient(Level level, Player player) {

	}

	@Override
	public void actionServer(Level level, ServerPlayer player) {
		CyclopsCore._instance.getPacketHandler().sendToPlayer(new SendPlayerNbtPacket(player), player);
	}
	
}