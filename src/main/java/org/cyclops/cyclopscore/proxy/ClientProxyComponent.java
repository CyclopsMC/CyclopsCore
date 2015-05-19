package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.apache.logging.log4j.Level;

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

	protected final Map<Class<? extends Entity>, Render> entityRenderers = Maps.newHashMap();
    protected final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> tileEntityRenderers = Maps.newHashMap();

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
			RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), entry.getValue());
            getMod().getLoggerHelper().log(Level.INFO, String.format("Registered %s renderer %s", entry.getKey(), entry.getValue()));
		}

		// Special TileEntity renderers
		for (Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : tileEntityRenderers.entrySet()) {
			ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
            getMod().getLoggerHelper().log(Level.INFO, String.format("Registered %s special renderer %s", entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public void registerKeyBindings() {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;

		// TODO: keyhandler

        getMod().getLoggerHelper().log(Level.INFO, "Registered key bindings");
	}

	@Override
	public void registerTickHandlers() {
        getMod().getLoggerHelper().log(Level.INFO, "Registered tick handlers");
	}
    
    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float frequency,
    		String mod) {
    	if(!SOUND_NONE.equals(sound)) {
	    	ResourceLocation soundLocation = new ResourceLocation(mod, sound);
	    	PositionedSoundRecord record = new PositionedSoundRecord(soundLocation,
					volume, frequency, (float) x, (float) y, (float) z);
	    	
	    	// If we notice this sound is no mod sound, relay it to the default MC sound system.
	    	if(!mod.equals(DEFAULT_RESOURCELOCATION_MOD) && FMLClientHandler.instance().getClient()
	    			.getSoundHandler().getSound(record.getSoundLocation()) == null) {
	    		playSoundMinecraft(x, y, z, sound, volume, frequency);
	    	} else {
		    	FMLClientHandler.instance().getClient().getSoundHandler()
		    		.playSound(record);
	    	}
    	}
    }
    
}
