package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
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
     * Register key bindings.
     * @param keyRegistry The key registry to register to.
     * @param event Register mappings event
     */
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event);

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
