package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.*;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigForge;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ConfigurableTypesForge {

    public static void load(ModBaseForge mod) {
        ConfigurableTypeCommon.BLOCK.setAction(new BlockActionForge<>(mod));
        ConfigurableTypeCommon.ITEM.setAction(new ItemActionForge<>(mod));
        ConfigurableTypeCommon.GUI.setAction(new GuiActionForge<>());
        ConfigurableTypeCommon.PARTICLE.setAction(new ParticleActionForge<>());
    }

    // Forge-specific
    public static final ConfigurableTypeCommon FLUID = new ConfigurableTypeCommon(true, FluidConfigForge.class, new FluidActionForge<>(), "fluid");
    public static final ConfigurableTypeCommon CONDITION = new ConfigurableTypeCommon(true, ConditionConfigForge.class, new ConditionActionForge<>(), "condition");
    public static final ConfigurableTypeCommon BIOME_MODIFIER = new ConfigurableTypeCommon(true, BiomeModifierConfigForge.class, new BiomeModifierActionForge<>(), "biome_modifier");
    public static final ConfigurableTypeCommon LOOT_MODIFIER = new ConfigurableTypeCommon(true, LootModifierConfigForge.class, new LootModifierActionForge<>(), "loot_modifier");

}
