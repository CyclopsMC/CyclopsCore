package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BiomeModifierActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.BlockActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.FluidActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.GuiActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.LootModifierActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.ParticleActionForge;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigForge;

/**
 * @author rubensworks
 */
public class ConfigurableTypesForge {

    public static void load() {
        ConfigurableTypeCommon.BLOCK.setAction(new BlockActionForge<>());
        ConfigurableTypeCommon.ITEM.setAction(new ItemActionForge<>());
        ConfigurableTypeCommon.GUI.setAction(new GuiActionForge<>());
        ConfigurableTypeCommon.PARTICLE.setAction(new ParticleActionForge<>());
    }

    // Forge-specific
    public static final ConfigurableTypeCommon FLUID = new ConfigurableTypeCommon(true, FluidConfigForge.class, new FluidActionForge<>(), "fluid");
    public static final ConfigurableTypeCommon BIOME_MODIFIER = new ConfigurableTypeCommon(true, BiomeModifierConfigForge.class, new BiomeModifierActionForge<>(), "biome_modifier");
    public static final ConfigurableTypeCommon LOOT_MODIFIER = new ConfigurableTypeCommon(true, LootModifierConfigForge.class, new LootModifierActionForge<>(), "loot_modifier");

}
