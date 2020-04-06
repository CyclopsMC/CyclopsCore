package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.Map;
import java.util.Map.Entry;
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
    protected final Map<TileEntityType<?>, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<?>>> tileEntityRenderers = Maps.newHashMap();

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
        this.iconProvider = constructIconProvider();
    }

    protected IconProvider constructIconProvider() {
        return new IconProvider(this);
    }

    @Override
    public <T extends TileEntity> void registerRenderer(TileEntityType<T> tileEntityType,
                                                        Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>> rendererFactory) {
        tileEntityRenderers.put(tileEntityType, rendererFactory);
    }

	@Override
	public void registerRenderers() {
		// Special TileEntity renderers
		for (Entry<TileEntityType<?>, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<?>>> entry : tileEntityRenderers.entrySet()) {
			ClientRegistry.bindTileEntityRenderer(entry.getKey(), (Function) entry.getValue());
            getMod().getLoggerHelper().log(Level.TRACE, String.format("Registered %s special renderer %s", entry.getKey(), entry.getValue()));
		}
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
