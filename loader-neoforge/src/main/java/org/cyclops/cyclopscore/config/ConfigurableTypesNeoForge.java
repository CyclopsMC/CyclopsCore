package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BiomeModifierAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.BlockActionNeoForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConditionAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.EntityAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.FluidAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemActionNeoForge;
import org.cyclops.cyclopscore.config.configurabletypeaction.LootModifierAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ParticleAction;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfig;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.config.extendedconfig.FluidConfig;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

/**
 * @author rubensworks
 */
public class ConfigurableTypesNeoForge {

    static {
        ConfigurableType.BLOCK.setAction(new BlockActionNeoForge<>());
        ConfigurableType.ITEM.setAction(new ItemActionNeoForge<>());
    }

    // The following could be generalized to common if needed
    public static final ConfigurableType ENTITY = new ConfigurableType(true, EntityConfig.class, new EntityAction<>(), "entity");
    public static final ConfigurableType PARTICLE = new ConfigurableType(true, ParticleConfig.class, new ParticleAction<>(), "particle");

    // NeoForge-specific
    public static final ConfigurableType GUI = new ConfigurableType(true, GuiConfig.class, new ConfigurableTypeActionForge<>(), "gui");
    public static final ConfigurableType FLUID = new ConfigurableType(true, FluidConfig.class, new FluidAction(), "fluid");
    public static final ConfigurableType CONDITION = new ConfigurableType(true, ConditionConfig.class, new ConditionAction<>(), "condition");
    public static final ConfigurableType BIOME_MODIFIER = new ConfigurableType(true, BiomeModifierConfig.class, new BiomeModifierAction<>(), "biome_modifier");
    public static final ConfigurableType LOOT_MODIFIER = new ConfigurableType(true, LootModifierConfig.class, new LootModifierAction<>(), "loot_modifier");

}
