package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;

/**
 * Config for capabilities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class CapabilityConfig<T> extends ExtendedConfig<CapabilityConfig<T>, T> {

    private final Class<T> type;
    private final Capability.IStorage<T> storage;
    private final Class<? extends T> implementation;

    /**
     * Make a new instance.
     * @param mod The mod
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param type The capability type.
     * @param storage The default capability storage.
     * @param implementation The default capability implementation
     */
    public CapabilityConfig(ModBase mod, boolean enabled, String namedId, String comment,
                            Class<T> type, Capability.IStorage<T> storage, Class<? extends T> implementation) {
        super(mod, enabled, namedId, comment, null);
        this.type = type;
        this.storage = storage;
        this.implementation = implementation;
    }
    
    @Override
    public String getUnlocalizedName() {
        return "capability." + getMod().getModId() + "." + getNamedId();
	}

    //Needed for config gui
    @Override
    public String getFullUnlocalizedName() {
        return getUnlocalizedName();
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.CAPABILITY;
	}

    public Class<T> getType() {
        return type;
    }

    public Capability.IStorage<T> getStorage() {
        return storage;
    }

    public Class<? extends T> getImplementation() {
        return implementation;
    }

    @Nullable
    @Override
    public IForgeRegistry<?> getRegistry() {
        return null;
    }
}
