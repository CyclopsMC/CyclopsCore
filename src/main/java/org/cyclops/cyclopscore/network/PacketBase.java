package org.cyclops.cyclopscore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * The base packet for packets.
 * All packets must have a default constructor.
 * @author rubensworks
 *
 */
public abstract class PacketBase {

    /**
     * @return If this packet can run on a thread other than the main-thread of Minecraft.
     *         If this is asynchronous, the player parameter inside the action is not guaranteed to be defined.
     */
	public abstract boolean isAsync();
	
	/**
	 * Encode this packet.
	 * @param output The byte array to encode to.
	 */
	public abstract void encode(PacketBuffer output);

	/**
	 * Decode for this packet.
	 * @param input The byte array to decode from.
	 */
    public abstract void decode(PacketBuffer input);

	/**
	 * Actions for client-side.
	 * @param world The world.
	 * @param player The player. Can be null if this packet is asynchronous.
	 */
	@OnlyIn(Dist.CLIENT)
    public abstract void actionClient(World world, PlayerEntity player);

	/**
	 * Actions for server-side.
	 * @param world The world.
	 * @param player The player.
	 */
    public abstract void actionServer(World world, ServerPlayerEntity player);

}
