package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.event.PlayerRingOfFire;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.world.gen.IRetroGenRegistry;

import java.util.function.Function;

/**
 * Base proxy for server and client side.
 * @author rubensworks
 *
 */
public abstract class CommonProxyComponent implements ICommonProxy {

    @Override
    public <T extends TileEntity> void registerRenderer(TileEntityType<T> tileEntityType,
                                                        Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>> rendererFactory) {
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
        IRetroGenRegistry retroGenRegistry = getMod().getRegistryManager().getRegistry(IRetroGenRegistry.class);
        if(retroGenRegistry != null) {
            MinecraftForge.EVENT_BUS.register(retroGenRegistry);
        }

        MinecraftForge.EVENT_BUS.register(new PlayerRingOfFire());
    }
}
