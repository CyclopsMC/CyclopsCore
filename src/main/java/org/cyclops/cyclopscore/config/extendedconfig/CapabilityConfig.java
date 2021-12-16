package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for capabilities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class CapabilityConfig<T> extends ExtendedConfig<CapabilityConfig<T>, T> {

    private final Class<T> type;
    private final Capability<T> capability = CapabilityManager.get(new CapabilityToken<>(){});

    /**
     * Make a new instance.
     * @param mod The mod
     * @param namedId The unique name ID for the configurable.
     * @param type The capability type.
     */
    public CapabilityConfig(ModBase mod, String namedId, Class<T> type) {
        super(mod, namedId, null);
        this.type = type;
    }
    
    @Override
    public String getTranslationKey() {
        return "capability." + getMod().getModId() + "." + getNamedId();
	}

    //Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.CAPABILITY;
	}

    public Class<T> getType() {
        return type;
    }

    public Capability<T> getCapability() {
        return capability;
    }
}
