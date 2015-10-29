package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for playing a sound at a location.
 * Override this to enable your mod.
 * @author rubensworks
 *
 */
public class SoundPacket extends PacketCodec {

	private static final int RANGE = 15;
	
	@CodecField
	private double x = 0;
    @CodecField
	private double y = 0;
    @CodecField
	private double z = 0;
    @CodecField
	private String mod = "";
	@CodecField
	private String sound = "";
    @CodecField
	private float volume = 0;
    @CodecField
	private float frequency = 0;
    
    /**
     * Empty packet.
     */
    public SoundPacket() {
    	
    }
    
    /**
	 * Creates a packet with coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
	 */
	public SoundPacket(double x, double y, double z, String sound, float volume, float frequency) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.sound = sound;
		this.volume = volume;
		this.frequency = frequency;
	}
	
	/**
	 * Creates a packet with coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
	 * @param mod The mod id that has this sound.
	 */
	public SoundPacket(double x, double y, double z, String sound, float volume, float frequency, String mod) {
		this(x, y, z, sound, volume, frequency);
		this.mod = mod;
	}
	
	/**
	 * Creates a packet which contains the location data.
	 * @param location The location data.
	 * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
	 */
	public SoundPacket(BlockPos location, String sound, float volume, float frequency) {
		this(location.getX(), location.getY(), location.getZ(),
				sound, volume, frequency);
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		CyclopsCore._instance.getProxy().playSound(x, y, z, sound, volume, frequency, mod);
	}
	@Override
	public void actionServer(World world, EntityPlayerMP player) {
        CyclopsCore._instance.getPacketHandler().sendToAllAround(new SoundPacket(x, y, z, sound, volume, frequency, mod),
				new NetworkRegistry.TargetPoint(world.provider.getDimensionId(), x, y, z, RANGE));
	}
	
}