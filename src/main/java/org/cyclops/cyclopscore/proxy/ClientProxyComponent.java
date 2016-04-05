package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
	
	protected static final String SOUND_NONE = "none";

	private final CommonProxyComponent commonProxyComponent;
	private final IconProvider iconProvider;
	protected final Map<Class<? extends Entity>, Render> entityRenderers = Maps.newHashMap();
    protected final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> tileEntityRenderers = Maps.newHashMap();

    public ClientProxyComponent(CommonProxyComponent commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
        this.iconProvider = constructIconProvider();
    }

    protected IconProvider constructIconProvider() {
        return new IconProvider(this);
    }

    @Override
    public void registerRenderer(Class<? extends Entity> clazz, Render renderer) {
        entityRenderers.put(clazz, renderer);
    }

    @Override
    public void registerRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer) {
        tileEntityRenderers.put(clazz, renderer);
    }

	@Override
	public void registerRenderers() {
		// Entity renderers
		for (Entry<Class<? extends Entity>, Render> entry : entityRenderers.entrySet()) {
            final Render render = entry.getValue();
            RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), new IRenderFactory<Entity>() {
                @Override
                public Render<? super Entity> createRenderFor(RenderManager manager) {
                    return render;
                }
            });
            getMod().getLoggerHelper().log(Level.TRACE, String.format("Registered %s renderer %s", entry.getKey(), entry.getValue()));
		}

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
    public void playSound(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency,
    		String mod) {
    	if(!SOUND_NONE.equals(sound)) {
	    	ResourceLocation soundLocation = new ResourceLocation(mod, sound);
	    	PositionedSoundRecord record = new PositionedSoundRecord(new SoundEvent(soundLocation), category,
					volume, frequency, (float) x, (float) y, (float) z);
	    	
	    	// If we notice this sound is no mod sound, relay it to the default MC sound system.
	    	if(!mod.equals(DEFAULT_RESOURCELOCATION_MOD) && !SoundEvent.soundEventRegistry.containsKey(soundLocation)) {
	    		playSoundMinecraft(x, y, z, sound, category, volume, frequency);
	    	} else {
		    	FMLClientHandler.instance().getClient().getSoundHandler()
		    		.playSound(record);
	    	}
    	}
    }
    
}
