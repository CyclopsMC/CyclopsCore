package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.ConditionActionFabric;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionRegistry;
import org.cyclops.cyclopscore.config.configurabletypeaction.GuiActionFabric;
import org.cyclops.cyclopscore.config.configurabletypeaction.ParticleActionFabric;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigFabric;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * @author rubensworks
 */
public class ConfigurableTypesFabric {

    public static void load() {
        ConfigurableTypeCommon.PARTICLE.setAction(new ParticleActionFabric<>());
        ConfigurableTypeCommon.GUI.setAction(new GuiActionFabric());
    }

    public static final ConfigurableTypeCommon GUI = new ConfigurableTypeCommon(true, GuiConfig.class, new ConfigurableTypeActionRegistry<>(), "gui");

    // Fabric-specific
    public static final ConfigurableTypeCommon CONDITION = new ConfigurableTypeCommon(true, ConditionConfigFabric.class, new ConditionActionFabric(), "condition");

}
