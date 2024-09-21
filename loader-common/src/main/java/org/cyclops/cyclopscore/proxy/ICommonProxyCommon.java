package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.network.IPacketHandler;


/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxyCommon {

    /**
     * @return The mod for this proxy.
     */
    public IModBase getMod();

    /**
     * Register a block entity renderer.
     * @param blockEntityType The block entity type.
     * @param rendererFactory The block entity render factory.
     * @param <T> The block entity type.
     */
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererFactory);

    /**
     * Register renderers.
     */
    public void registerRenderers();

    /**
     * Register tick handlers.
     */
    public void registerTickHandlers();

    /**
     * Register the event hooks
     */
    public void registerEventHooks();

    public void registerPackets(IPacketHandler packetHandler);

}
