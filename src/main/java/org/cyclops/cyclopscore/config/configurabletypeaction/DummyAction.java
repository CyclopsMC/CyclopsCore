package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

/**
 * Just a dummy action.
 * @author rubensworks
 *
 */
public class DummyAction extends ConfigurableTypeAction<DummyConfig, Object> {

    @Override
    public void preRun(DummyConfig eConfig, Configuration config, boolean startup) {
        
    }

    @Override
    public void postRun(DummyConfig eConfig, Configuration config) {
        
    }

}
