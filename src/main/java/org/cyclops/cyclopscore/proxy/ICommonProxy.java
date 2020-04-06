package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.function.Function;

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
     * @param tileEntityType The tile entity type.
     * @param rendererFactory The tile entity render factory.
     * @param <T> The tile entity type.
     */
    public <T extends TileEntity> void registerRenderer(TileEntityType<T> tileEntityType,
                                                        Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>> rendererFactory);

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
