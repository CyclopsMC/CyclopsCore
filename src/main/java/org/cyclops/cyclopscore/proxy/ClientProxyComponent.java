package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.icon.IconProvider;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Base proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class ClientProxyComponent extends CommonProxyComponent implements ICommonProxy {

	private final CommonProxyComponent commonProxyComponent;
	private final IconProvider iconProvider;
    protected final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> tileEntityRenderers = Maps.newHashMap();

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
        this.iconProvider = constructIconProvider();
    }

    protected IconProvider constructIconProvider() {
        return new IconProvider(this);
    }

    @Override
    public void registerRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer) {
        tileEntityRenderers.put(clazz, renderer);
    }

	@Override
	public void registerRenderers() {
		// Special TileEntity renderers
		for (Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : tileEntityRenderers.entrySet()) {
			ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
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
    
    @Override
    public void playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float frequency) {
		PositionedSoundRecord record = new PositionedSoundRecord(sound, category,
				volume, frequency, (float) x, (float) y, (float) z);
		FMLClientHandler.instance().getClient().getSoundHandler()
				.playSound(record);
    }
    
}
