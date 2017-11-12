package org.cyclops.cyclopscore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The base packet for packets.
 * @author rubensworks
 *
 */
public abstract class PacketBase implements IMessage {

    /**
     * @return If this packet can run on a thread other than the main-thread of Minecraft.
     *         If this is asynchronous, the player parameter inside the action is not guaranteed to be defined.
     */
	public abstract boolean isAsync();
	
	/**
	 * Encode this packet.
	 * @param output The byte array to encode to.
	 */
	public abstract void encode(ExtendedBuffer output);

	/**
	 * Decode for this packet.
	 * @param input The byte array to decode from.
	 */
    public abstract void decode(ExtendedBuffer input);

	/**
	 * Actions for client-side.
	 * @param world The world.
	 * @param player The player. Can be null if this packet is asynchronous.
	 */
	@SideOnly(Side.CLIENT)
    public abstract void actionClient(World world, EntityPlayer player);

	/**
	 * Actions for server-side.
	 * @param world The world.
	 * @param player The player.
	 */
    public abstract void actionServer(World world, EntityPlayerMP player);

	@Override
	public void fromBytes(ByteBuf source) {
		decode(new ExtendedBuffer(source));
	}

	@Override
	public void toBytes(ByteBuf target) {
		encode(new ExtendedBuffer(target));
	}

}
