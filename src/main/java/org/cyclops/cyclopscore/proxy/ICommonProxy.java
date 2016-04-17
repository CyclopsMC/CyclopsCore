package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
     * Play a sound, will do nothing serverside, use {@link CommonProxyComponent#sendSound(double,
     * double, double, SoundEvent, SoundCategory, float, float)} for this.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param category The sound category.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float frequency);

    /**
     * Send an evilcraft sound packet.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @param sound The sound name to play.
     * @param category The sound category.
     * @param volume The volume of the sound.
     * @param frequency The pitch of the sound.
     */
    public void sendSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float frequency);

}
