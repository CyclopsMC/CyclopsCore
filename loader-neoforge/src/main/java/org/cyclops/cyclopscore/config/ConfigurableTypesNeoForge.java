package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.*;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfigNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigNeoForge;

/**
 * @author rubensworks
 */
public class ConfigurableTypesNeoForge {

    public static void load() {
        ConfigurableTypeCommon.BLOCK.setAction(new BlockActionNeoForge());
        ConfigurableTypeCommon.ITEM.setAction(new ItemActionNeoForge<>());
        ConfigurableTypeCommon.GUI.setAction(new GuiActionNeoForge<>());
        ConfigurableTypeCommon.PARTICLE.setAction(new ParticleActionNeoForge<>());
    }

    // NeoForge-specific
    public static final ConfigurableTypeCommon FLUID = new ConfigurableTypeCommon(true, FluidConfigNeoForge.class, new FluidActionNeoForge(), "fluid");
    public static final ConfigurableTypeCommon CONDITION = new ConfigurableTypeCommon(true, ConditionConfigNeoForge.class, new ConditionActionNeoForge<>(), "condition");
    public static final ConfigurableTypeCommon BIOME_MODIFIER = new ConfigurableTypeCommon(true, BiomeModifierConfigNeoForge.class, new BiomeModifierActionNeoForge<>(), "biome_modifier");
    public static final ConfigurableTypeCommon LOOT_MODIFIER = new ConfigurableTypeCommon(true, LootModifierConfigNeoForge.class, new LootModifierActionNeoForge<>(), "loot_modifier");

}
