package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;

/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxy {

    /**
     * @return The mod for this proxy.
     */
    public ModBase getMod();

    /**
     * Register an entity renderer.
     * @param clazz The entity class.
     * @param renderer The entity renderer.
     */
    public void registerRenderer(Class<? extends Entity> clazz, Render renderer);

    /**
     * Register a tile entity renderer.
     * @param clazz The tile entity class.
     * @param renderer The tile entity renderer.
     */
    public void registerRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer);

    /**
     * Register renderers.
     */
    public void registerRenderers();

    /**
     * Register key bindings.
     * @param keyRegistry The key registry to register to.
     */
    public void registerKeyBindings(IKeyRegistry keyRegistry);

    /**
     * Register packet handlers.
     * @param packetHandler The packet handler.
     */
    public void registerPacketHandlers(PacketHandler packetHandler);

    /**
     * Register tick handlers.
     */
    public void registerTickHandlers();

    /**
     * Register the event hooks
     */
    public void registerEventHooks();

    /**
     * Play a minecraft sound, will do nothing serverside, use {@link CommonProxyComponent#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param pos The location.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSoundMinecraft(BlockPos pos, String sound, float volume, float frequency);

    /**
     * Play a minecraft sound, will do nothing serverside, use {@link CommonProxyComponent#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSoundMinecraft(double x, double y, double z, String sound, float volume, float frequency);

    /**
     * Play a sound, will do nothing serverside, use {@link CommonProxyComponent#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     * @param mod The mod that has this sound.
     */
    public void playSound(double x, double y, double z, String sound, float volume, float frequency,
                          String mod);

    /**
     * Play an evilcraft sound, will do nothing serverside, use {@link CommonProxyComponent#sendSound(double,
     * double, double, String, float, float, String)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSound(double x, double y, double z, String sound, float volume, float frequency);

    /**
     * Send a minecraft sound packet.
     * @param pos The location.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSoundMinecraft(BlockPos pos, String sound, float volume, float frequency);

    /**
     * Send a minecraft sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSoundMinecraft(double x, double y, double z, String sound, float volume, float frequency);

    /**
     * Send a sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     * @param mod The mod id that has this sound.
     */
    public void sendSound(double x, double y, double z, String sound, float volume, float frequency,
                          String mod);

    /**
     * Send an evilcraft sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSound(double x, double y, double z, String sound, float volume, float frequency);

}
