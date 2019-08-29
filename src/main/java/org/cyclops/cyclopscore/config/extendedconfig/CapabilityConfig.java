package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.concurrent.Callable;

/**
 * Config for capabilities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class CapabilityConfig<T> extends ExtendedConfig<CapabilityConfig<T>, T> {

    private final Class<T> type;
    private final Capability.IStorage<T> storage;
    private final Callable<? extends T> implementation;

    /**
     * Make a new instance.
     * @param mod The mod
     * @param namedId The unique name ID for the configurable.
     * @param type The capability type.
     * @param storage The default capability storage.
     * @param implementation The default capability implementation
     */
    public CapabilityConfig(ModBase mod, String namedId, Class<T> type,
                            Capability.IStorage<T> storage, Callable<? extends T> implementation) {
        super(mod, namedId, null);
        this.type = type;
        this.storage = storage;
        this.implementation = implementation;
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

    public Capability.IStorage<T> getStorage() {
        return storage;
    }

    public Callable<? extends T> getFactory() {
        return implementation;
    }

}
