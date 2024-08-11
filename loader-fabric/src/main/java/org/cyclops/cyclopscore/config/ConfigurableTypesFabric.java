package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionForge;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * @author rubensworks
 */
public class ConfigurableTypesFabric {

    public static final ConfigurableType GUI = new ConfigurableType(true, GuiConfig.class, new ConfigurableTypeActionForge<>(), "gui");

}