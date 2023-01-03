package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.Map;

/**
 * Base proxy for the client side.
 *
 * @author rubensworks
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class ClientProxyComponent extends CommonProxyComponent implements ICommonProxy, IClientProxy {

    private final CommonProxyComponent commonProxyComponent;
    protected final Map<BlockEntityType, BlockEntityRendererProvider> blockEntityRenderers = Maps.newHashMap();

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
    }

    @Override
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> rendererFactory) {
        blockEntityRenderers.put(blockEntityType, rendererFactory);
    }

    @Override
    public void registerRenderers() {
        // Special BlockEntity renderers
        for (Map.Entry<BlockEntityType, BlockEntityRendererProvider> entry : blockEntityRenderers.entrySet()) {
            BlockEntityRenderers.register(entry.getKey(), entry.getValue());
            getMod().getLoggerHelper().log(Level.TRACE, String.format("Registered %s special renderer %s", entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event) {
        getMod().getLoggerHelper().log(Level.TRACE, "Registered key bindings");
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        commonProxyComponent.registerPacketHandlers(packetHandler);
        getMod().getLoggerHelper().log(Level.TRACE, "Registered packet handlers");
    }

    @Override
    public void registerTickHandlers() {
        commonProxyComponent.registerTickHandlers();
        getMod().getLoggerHelper().log(Level.TRACE, "Registered tick handlers");
    }

    @Override
    public void registerEventHooks() {
        commonProxyComponent.registerEventHooks();
        getMod().getLoggerHelper().log(Level.TRACE, "Registered event hooks");
        MinecraftForge.EVENT_BUS.register(getMod().getKeyRegistry());
    }

}
