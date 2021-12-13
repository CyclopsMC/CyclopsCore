package org.cyclops.cyclopscore.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
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
	public void actionClient(World world, PlayerEntity player) {
		long start = System.currentTimeMillis();
		Minecraft.getInstance().reloadResourcePacks();
		long end = System.currentTimeMillis();
		player.sendMessage(new StringTextComponent(String.format("Reloaded all resources in %s ms", end - start)), Util.NIL_UUID);
	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {

	}
	
}