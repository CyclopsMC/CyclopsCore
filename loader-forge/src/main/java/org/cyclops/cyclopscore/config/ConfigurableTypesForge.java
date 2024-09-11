package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BiomeModifierActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.BlockActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.CreativeModeTabActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.FluidActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemActionForge;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;

/**
 * @author rubensworks
 */
public class ConfigurableTypesForge {

    static {
        ConfigurableTypeCommon.BLOCK.setAction(new BlockActionForge<>());
        ConfigurableTypeCommon.ITEM.setAction(new ItemActionForge<>());
        ConfigurableTypeCommon.CREATIVE_MODE_TAB.setAction(new CreativeModeTabActionForge<>());
    }

    // Forge-specific
    public static final ConfigurableTypeCommon FLUID = new ConfigurableTypeCommon(true, FluidConfigForge.class, new FluidActionForge<>(), "fluid");
    public static final ConfigurableTypeCommon BIOME_MODIFIER = new ConfigurableTypeCommon(true, BiomeModifierConfigForge.class, new BiomeModifierActionForge<>(), "biome_modifier");

}
