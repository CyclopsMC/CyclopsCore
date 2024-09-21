package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.network.IPacketHandler;

/**
 * Base proxy for server and client side.
 * @author rubensworks
 *
 */
public abstract class CommonProxyComponentCommon implements ICommonProxyCommon {
    @Override
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererFactory) {
        throw new IllegalArgumentException("Registration of renderers should not be called server side!");
    }

    @Override
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }

    @Override
    public void registerTickHandlers() {

    }

    @Override
    public void registerEventHooks() {

    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {

    }
}
