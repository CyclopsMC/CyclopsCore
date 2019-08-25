package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
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
    public void registerRenderer(Class<? extends TileEntity> clazz, TileEntityRenderer renderer);

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

}
