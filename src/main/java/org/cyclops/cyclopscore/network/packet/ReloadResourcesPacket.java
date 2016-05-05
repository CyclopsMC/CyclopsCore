package org.cyclops.cyclopscore.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		long start = System.currentTimeMillis();
		Minecraft.getMinecraft().refreshResources();
		long end = System.currentTimeMillis();
		player.addChatMessage(new ChatComponentText(String.format("Reloaded all resources in %s ms", end - start)));
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {

	}
	
}