package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BlockActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.CreativeModeTabActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.FluidActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemActionForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;

/**
 * @author rubensworks
 */
public class ConfigurableTypesForge {

    static {
        ConfigurableType.BLOCK.setAction(new BlockActionForge<>());
        ConfigurableType.ITEM.setAction(new ItemActionForge<>());
        ConfigurableType.CREATIVE_MODE_TAB.setAction(new CreativeModeTabActionForge<>());
    }

    // Forge-specific
    public static final ConfigurableType FLUID = new ConfigurableType(true, FluidConfigForge.class, new FluidActionForge<>(), "fluid");

}
