package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionRegistry;
import org.cyclops.cyclopscore.config.configurabletypeaction.ParticleActionFabric;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * @author rubensworks
 */
public class ConfigurableTypesFabric {

    static {
        ConfigurableTypeCommon.PARTICLE.setAction(new ParticleActionFabric<>());
    }

    public static final ConfigurableTypeCommon GUI = new ConfigurableTypeCommon(true, GuiConfig.class, new ConfigurableTypeActionRegistry<>(), "gui");

}
