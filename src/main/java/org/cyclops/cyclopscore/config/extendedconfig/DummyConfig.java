package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;

/**
 * Dummy config.
 * @author rubensworks
 * @see ExtendedConfig
 *
 */
public class DummyConfig extends ExtendedConfig<DummyConfig, Object>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public DummyConfig(ModBase mod, boolean enabled, String namedId,
            String comment, Class<?> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.DUMMY;
	}

	@Override
	public String getUnlocalizedName() {
		return getNamedId();
	}

	@Nullable
	@Override
	public IForgeRegistry<?> getRegistry() {
		return null;
	}
}
