package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Dummy config.
 * @author rubensworks
 * @see ExtendedConfig
 *
 */
public class DummyConfig extends ExtendedConfig<DummyConfig, Void>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
	 */
	public DummyConfig(ModBase mod, boolean enabled, String namedId, String comment) {
		super(mod, enabled, namedId, comment, (c) -> null);
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.DUMMY;
	}

	@Override
	public String getTranslationKey() {
		return getNamedId();
	}

}
