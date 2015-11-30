package org.cyclops.cyclopscore.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Create config file and register items and blocks from the given ExtendedConfigs.
 * @author rubensworks
 *
 */
@SuppressWarnings("rawtypes")
@EqualsAndHashCode(callSuper = true, exclude = {"config", "mod"})
@Data
public class ConfigHandler extends LinkedHashSet<ExtendedConfig> {

    private final ModBase mod;
    private Configuration config;
    private final LinkedHashSet<ExtendedConfig> processedConfigs = new LinkedHashSet<ExtendedConfig>();
    private final Map<String, ExtendedConfig> configDictionary = Maps.newHashMap();
    private final Set<String> categories = Sets.newHashSet();
    private final Map<String, ConfigProperty> commandableProperties = Maps.newHashMap();
    
    @Override
    public boolean add(ExtendedConfig e) {
    	configDictionary.put(e.getNamedId(), e);
    	return super.add(e);
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements
     * This also sets the config of this instance.
     * @param event the event from the init methods
     */
    public void handle(FMLPreInitializationEvent event) {
        if(getConfig() == null) {
            // You will be able to find the config file in .minecraft/config/ and it will be named EvilCraft.cfg
            // here our Configuration has been instantiated, and saved under the name "config"
            // If the file doesn't already exist, it will be created.
            Configuration config = new Configuration(event.getSuggestedConfigurationFile());
            setConfig(config);

            // Loading the configuration from its file
            config.load();
        }
        
        loadConfig();
    }

    /**
     * Add a config category.
     * @param category The category to add.
     */
    public void addCategory(String category) {
        categories.add(category);
    }
    
    /**
     * Iterate over the given ExtendedConfigs to read/write the config and register the given elements.
     */
    @SuppressWarnings("unchecked")
    public void loadConfig() {
        for(ExtendedConfig<?> eConfig : this) {
            addCategory(eConfig.getHolderType().getCategory());
            if(!eConfig.isHardDisabled()) {
                // Save additional properties
                for(ConfigProperty configProperty : eConfig.configProperties) {
                    categories.add(configProperty.getCategory());
                    configProperty.save(config);
                    if(configProperty.isCommandable()) {
                        commandableProperties.put(configProperty.getName(), configProperty);
                    }
                }
                
                // Register the element depending on the type.
                ConfigurableType type = eConfig.getHolderType();
                type.getElementTypeAction().commonRun(eConfig, config);
                
                if(eConfig.isEnabled()) {
	                // Call the listener
	                eConfig.onRegistered();
	
	                mod.log(Level.TRACE, "Registered " + eConfig.getNamedId());
	                processedConfigs.add(eConfig);
	                
	                // Register as init listener.
	                mod.addInitListeners(new ConfigInitListener(eConfig));
                }
            }
        }
        
        // Empty the configs so they won't be loaded again later
        this.removeAll(this);
        
        // Saving the configuration to its file
        config.save();
    }

    /**
     * Polish the enabled configs during the initialization phase.
     */
    @SuppressWarnings("unchecked")
    public void polishConfigs() {
        for(ExtendedConfig<?> eConfig : processedConfigs) {
            ConfigurableType type = eConfig.getHolderType();
            type.getElementTypeAction().polish(eConfig);
        }
    }
    
    /**
     * Sync the config values that were already loaded.
     * This will update the values in-game and in the config file.
     */
    @SuppressWarnings("unchecked")
	public void syncProcessedConfigs() {
    	for(ExtendedConfig<?> eConfig : processedConfigs) {
    		// Re-save additional properties
            for(ConfigProperty configProperty : eConfig.configProperties) {
                configProperty.save(config, false);
            }
            
            // Register the element depending on the type.
            ConfigurableType type = eConfig.getHolderType();
            type.getElementTypeAction().preRun(eConfig, config, false);
    	}

        // Update the config file.
        getConfig().save();
    }

	/**
	 * @return the config
	 */
	public Configuration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Configuration config) {
		this.config = config;
	}
	
	/**
	 * Get the map of config nameid to config.
	 * @return The dictionary.
	 */
	public Map<String, ExtendedConfig> getDictionary() {
		return configDictionary;
	}
	
	/**
	 * Init listener for configs.
	 * @author rubensworks
	 *
	 */
	public static class ConfigInitListener implements IInitListener {

		private ExtendedConfig<?> config;
		
		/**
		 * Make a new instance.
		 * @param config The config.
		 */
		public ConfigInitListener(ExtendedConfig<?> config) {
			this.config = config;
		}
		
		@Override
		public void onInit(IInitListener.Step step) {
			config.onInit(step);
			if(step == IInitListener.Step.POSTINIT) {
				for(ConfigProperty property : config.configProperties) {
					IChangedCallback changedCallback = property.getCallback().getChangedCallback();
					if(changedCallback != null) {
                        changedCallback.onRegisteredPostInit(property.getValue());
					}
				}
			}
		}
		
	}

    /**
     * A safe way to check if a {@link org.cyclops.cyclopscore.config.configurable.IConfigurable} is enabled. @see ExtendedConfig#isEnabled()
     * @param config The config to check.
     * @return If the given config is enabled.
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEnabled(Class<? extends ExtendedConfig> config) {
        try {
            return ((ExtendedConfig)config.getField("_instance").get(null)).isEnabled();
        } catch (NullPointerException e1) {
            return false;
        } catch (IllegalArgumentException e2) {
            return false;
        } catch (IllegalAccessException e3) {
            return false;
        } catch (NoSuchFieldException e3) {
            return false;
        } catch (SecurityException e4) {
            return false;
        }
    }

    /**
     * Get the config from a given item.
     * It will internally also try to get the blockState from the item if it exists to get the config from.
     * @param item The item, possibly IConfigurable.
     * @return The config or null.
     */
    public static @Nullable ExtendedConfig<?> getConfigFromItem(Item item) {
        if(item instanceof IConfigurable) {
            return ((IConfigurable) item).getConfig();
        } else {
            Block block = Block.getBlockFromItem(item);
            if(block != Blocks.air && block instanceof IConfigurable) {
                return ((IConfigurable) block).getConfig();
            } else {
                return null;
            }
        }
    }
    
}
