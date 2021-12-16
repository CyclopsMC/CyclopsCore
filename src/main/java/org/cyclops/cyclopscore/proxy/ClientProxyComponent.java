package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.Map;
import java.util.function.Function;

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
	private final IconProvider iconProvider;
    protected final Map<BlockEntityType<?>, Function<? super BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<?>>> tileEntityRenderers = Maps.newHashMap();

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
        this.iconProvider = constructIconProvider();
    }

    protected IconProvider constructIconProvider() {
        return new IconProvider(this);
    }

    @Override
    public <T extends BlockEntity> void registerRenderer(BlockEntityType<T> tileEntityType,
                                                        Function<? super BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<? super T>> rendererFactory) {
        tileEntityRenderers.put(tileEntityType, rendererFactory);
    }

	@Override
	public void registerRenderers() {
		// Special TileEntity renderers
        // TODO: do using EntityRenderersEvent.RegisterRenderers.registerBlockEntityRenderer
		/*for (Entry<BlockEntityType<?>, Function<? super BlockEntityRenderDispatcher, ? extends BlockEntityRenderer<?>>> entry : tileEntityRenderers.entrySet()) {
			ClientRegistry.bindTileEntityRenderer(entry.getKey(), (Function) entry.getValue());
            getMod().getLoggerHelper().log(Level.TRACE, String.format("Registered %s special renderer %s", entry.getKey(), entry.getValue()));
		}*/
	}

	@Override
	public void registerKeyBindings(IKeyRegistry keyRegistry) {
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
