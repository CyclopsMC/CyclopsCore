package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.*;
import org.cyclops.cyclopscore.config.extendedconfig.*;

/**
 * @author rubensworks
 */
public class ConfigurableTypesNeoForge {

    // The following could be generalized to common if needed
    public static final ConfigurableType PARTICLE = new ConfigurableType(true, ParticleConfig.class, new ParticleAction<>(), "particle");

    // NeoForge-specific
    public static final ConfigurableType GUI = new ConfigurableType(true, GuiConfig.class, new ConfigurableTypeActionForge<>(), "gui");
    public static final ConfigurableType FLUID = new ConfigurableType(true, FluidConfig.class, new FluidAction(), "fluid");
    public static final ConfigurableType CONDITION = new ConfigurableType(true, ConditionConfig.class, new ConditionAction<>(), "condition");
    public static final ConfigurableType BIOME_MODIFIER = new ConfigurableType(true, BiomeModifierConfig.class, new BiomeModifierAction<>(), "biome_modifier");
    public static final ConfigurableType LOOT_MODIFIER = new ConfigurableType(true, LootModifierConfig.class, new LootModifierAction<>(), "loot_modifier");

    // Deprecated config types
    // TODO: rm in next major
    public static final ConfigurableType D_EFFECT = new ConfigurableType(true, EffectConfig.class, new ConfigurableTypeActionForge<>(), "potion");
    public static final ConfigurableType D_ARGUMENT_TYPE = new ConfigurableType(true, ArgumentTypeConfig.class, new ConfigurableTypeActionForge<>(), "argument_type");
    public static final ConfigurableType D_ARMOR_MATERIAL = new ConfigurableType(true, ArmorMaterialConfig.class, new ConfigurableTypeActionForge<>(), "armor_material");
    public static final ConfigurableType D_BLOCK_ENTITY = new ConfigurableType(true, BlockEntityConfig.class, new ConfigurableTypeActionForge<>(), "block_entity");
    public static final ConfigurableType D_CAPABILITY = new ConfigurableType(false, CapabilityConfig.class, new CapabilityActionOld<>(), "capability");
    public static final ConfigurableType D_CREATIVE_MODE_TAB = new ConfigurableType(true, CreativeModeTabConfig.class, new ConfigurableTypeActionForge<>(), "creative_mode_tab");
    public static final ConfigurableType D_CRITERION_TRIGGER = new ConfigurableType(true, CriterionTriggerConfig.class, new ConfigurableTypeActionForge<>(), "criterion_trigger");
    public static final ConfigurableType D_DATA_COMPONENT = new ConfigurableType(true, DataComponentConfig.class, new ConfigurableTypeActionForge<>(), "data_component");
    public static final ConfigurableType D_ENCHANTMENT_EFFECT_COMPONENT = new ConfigurableType(true, EnchantmentEffectComponentConfig.class, new ConfigurableTypeActionForge<>(), "enchantment_entity_effect");
    public static final ConfigurableType D_ENCHANTMENT_ENTITY_EFFECT = new ConfigurableType(true, EnchantmentEntityEffectConfig.class, new ConfigurableTypeActionForge<>(), "enchantment_entity_effect");
    public static final ConfigurableType D_FOLIAGE_PLACER = new ConfigurableType(true, FoliagePlacerConfig.class, new ConfigurableTypeActionForge<>(), "foliage_placer");
    public static final ConfigurableType D_LOOT_CONDITION = new ConfigurableType(true, LootConditionConfig.class, new ConfigurableTypeActionForge<>(), "loot_condition");
    public static final ConfigurableType D_LOOT_FUNCTION = new ConfigurableType(true, LootFunctionConfig.class, new ConfigurableTypeActionForge<>(), "loot_function");
    public static final ConfigurableType D_LOOT_NBT_PROVIDER = new ConfigurableType(true, LootNbtProviderConfig.class, new ConfigurableTypeActionForge<>(), "loot_nbt_provider");
    public static final ConfigurableType D_LOOT_NUMBER_PROVIDER = new ConfigurableType(true, LootNumberProviderConfig.class, new ConfigurableTypeActionForge<>(), "loot_number_provider");
    public static final ConfigurableType D_LOOT_SCORE_PROVIDER = new ConfigurableType(true, LootScoreProviderConfig.class, new ConfigurableTypeActionForge<>(), "loot_score_provider");
    public static final ConfigurableType D_RECIPE = new ConfigurableType(true, RecipeConfig.class, new ConfigurableTypeActionForge<>(), "recipe");
    public static final ConfigurableType D_RECIPE_TYPE = new ConfigurableType(true, RecipeTypeConfig.class, new ConfigurableTypeActionForge<>(), "recipe_type");
    public static final ConfigurableType D_SOUND_EVENT = new ConfigurableType(true, SoundEventConfig.class, new ConfigurableTypeActionForge<>(), "sound_event");
    public static final ConfigurableType D_TRUNK_PLACER = new ConfigurableType(true, TrunkPlacerConfig.class, new ConfigurableTypeActionForge<>(), "trunk_placer");
    public static final ConfigurableType D_VILLAGER = new ConfigurableType(true, VillagerConfig.class, new ConfigurableTypeActionForge<>(), "mob");
    public static final ConfigurableType D_WORLD_FEATURE = new ConfigurableType(true, WorldFeatureConfig.class, new ConfigurableTypeActionForge<>(), "world_feature");
    public static final ConfigurableType D_WORLD_STRUCTURE = new ConfigurableType(true, WorldStructureConfig.class, new ConfigurableTypeActionForge<>(), "world_structure");
    public static final ConfigurableType D_WORLD_STRUCTURE_PIECE = new ConfigurableType(true, WorldStructurePieceConfig.class, new ConfigurableTypeActionForge<>(), "world_structure_piece");
    public static final ConfigurableType D_BLOCK = new ConfigurableType(true, BlockConfig.class, new BlockActionNeoForge(), "block");
    public static final ConfigurableType D_ITEM = new ConfigurableType(true, ItemConfig.class, new ItemActionNeoForge(), "item");
    public static final ConfigurableType ENTITY = new ConfigurableType(true, EntityConfig.class, new EntityAction<>(), "entity");
    public static final ConfigurableType D_DUMMY = new ConfigurableType(false, DummyConfig.class, new DummyAction(), "general");

}
