package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Dummy config.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 *
 */
public class DummyConfigCommon<M extends IModBase> extends ExtendedConfig<DummyConfigCommon<M>, Void, M>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     */
    public DummyConfigCommon(M mod, String namedId) {
        super(mod, namedId, (c) -> null);
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
