package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.init.ModBase;

/**
 * Dummy config.
 * @author rubensworks
 * @see ExtendedConfigCommon
 *
 */
@Deprecated // TODO: rm in next major
public class DummyConfig extends DummyConfigCommon<ModBase<?>>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     */
    public DummyConfig(ModBase<?> mod, String namedId) {
        super(mod, namedId);
    }

}
