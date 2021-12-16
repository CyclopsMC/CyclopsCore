package org.cyclops.cyclopscore.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for reloading resources for a player.
 * @author rubensworks
 *
 */
public class ReloadResourcesPacket extends PacketCodec {

    /**
     * Empty packet.
     */
    public ReloadResourcesPacket() {

    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(Level level, Player player) {
		long start = System.currentTimeMillis();
		Minecraft.getInstance().reloadResourcePacks();
		long end = System.currentTimeMillis();
		player.sendMessage(new TextComponent(String.format("Reloaded all resources in %s ms", end - start)), Util.NIL_UUID);
	}

	@Override
	public void actionServer(Level level, ServerPlayer player) {

	}
	
}