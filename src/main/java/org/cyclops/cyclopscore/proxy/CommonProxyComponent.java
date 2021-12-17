package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.event.PlayerRingOfFire;
import org.cyclops.cyclopscore.network.PacketHandler;

/**
 * Base proxy for server and client side.
 * @author rubensworks
 *
 */
public abstract class CommonProxyComponent implements ICommonProxy {

    @Override
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererFactory) {
        throw new IllegalArgumentException("Registration of renderers should not be called server side!");
    }

    @Override
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry) {
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {

    }

    @Override
    public void registerTickHandlers() {

    }

    @Override
    public void registerEventHooks() {
        MinecraftForge.EVENT_BUS.register(new PlayerRingOfFire());
    }
}
