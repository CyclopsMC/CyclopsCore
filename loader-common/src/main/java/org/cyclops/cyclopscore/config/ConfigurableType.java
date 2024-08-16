package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BlockAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.CapabilityAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionRegistry;
import org.cyclops.cyclopscore.config.configurabletypeaction.DummyAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemAction;
import org.cyclops.cyclopscore.config.extendedconfig.*;

/**
 * The different types of configurable.
 * @author rubensworks
 *
 */
public class ConfigurableType {

    public static final ConfigurableType ITEM = new ConfigurableType(true, ItemConfigCommon.class, new ItemAction<>(), "item");
    public static final ConfigurableType BLOCK = new ConfigurableType(true, BlockConfigCommon.class, new BlockAction<>(), "block");
    public static final ConfigurableType VILLAGER = new ConfigurableType(true, VillagerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "mob");
    public static final ConfigurableType EFFECT = new ConfigurableType(true, EffectConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "potion");
    public static final ConfigurableType CAPABILITY = new ConfigurableType(false, CapabilityConfigCommon.class, new CapabilityAction<>(), "capability");
    public static final ConfigurableType RECIPE = new ConfigurableType(true, RecipeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "recipe");
    public static final ConfigurableType RECIPE_TYPE = new ConfigurableType(true, RecipeTypeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "recipe_type");
    public static final ConfigurableType BLOCK_ENTITY = new ConfigurableType(true, BlockEntityConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "block_entity");
    public static final ConfigurableType WORLD_FEATURE = new ConfigurableType(true, WorldFeatureConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_feature");
    public static final ConfigurableType WORLD_STRUCTURE = new ConfigurableType(true, WorldStructureConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_structure");
    public static final ConfigurableType WORLD_STRUCTURE_PIECE = new ConfigurableType(true, WorldStructurePieceConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_structure_piece");
    public static final ConfigurableType FOLIAGE_PLACER = new ConfigurableType(true, FoliagePlacerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "foliage_placer");
    public static final ConfigurableType TRUNK_PLACER = new ConfigurableType(true, TrunkPlacerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "trunk_placer");
    public static final ConfigurableType ARGUMENT_TYPE = new ConfigurableType(true, ArgumentTypeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "argument_type");
    public static final ConfigurableType CREATIVE_MODE_TAB = new ConfigurableType(true, CreativeModeTabConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "creative_mode_tab");
    public static final ConfigurableType CRITERION_TRIGGER = new ConfigurableType(true, CriterionTriggerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "criterion_trigger");
    public static final ConfigurableType LOOT_FUNCTION = new ConfigurableType(true, LootFunctionConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_function");
    public static final ConfigurableType LOOT_CONDITION = new ConfigurableType(true, LootConditionConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_condition");
    public static final ConfigurableType LOOT_NUMBER_PROVIDER = new ConfigurableType(true, LootNumberProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_number_provider");
    public static final ConfigurableType LOOT_NBT_PROVIDER = new ConfigurableType(true, LootNbtProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_nbt_provider");
    public static final ConfigurableType LOOT_SCORE_PROVIDER = new ConfigurableType(true, LootScoreProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_score_provider");
    public static final ConfigurableType SOUND_EVENT = new ConfigurableType(true, SoundEventConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "sound_event");
    public static final ConfigurableType DATA_COMPONENT = new ConfigurableType(true, DataComponentConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "data_component");
    public static final ConfigurableType ARMOR_MATERIAL = new ConfigurableType(true, ArmorMaterialConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "armor_material");
    public static final ConfigurableType ENCHANTMENT_ENTITY_EFFECT = new ConfigurableType(true, EnchantmentEntityEffectConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "enchantment_entity_effect");

    /**
     * Dummy type, only used for configs that refer to nothing.
     */
    public static final ConfigurableType DUMMY = new ConfigurableType(false, DummyConfigCommon.class, new DummyAction<>(), "general");

    private final boolean uniqueInstance;
    @SuppressWarnings("rawtypes")
    private final Class<? extends ExtendedConfig> configClass;
    @SuppressWarnings("rawtypes")
    private ConfigurableTypeAction action;
    private final String category;

    /**
     * Make a new instance.
     * @param uniqueInstance If this type has a unique instance for each config.
     * @param configClass The config class.
     * @param action The action instance that helps with saving of the config and optional instance.
     * @param category The category in which the configs should be saved.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableType(boolean uniqueInstance, Class<? extends ExtendedConfig> configClass,
                            ConfigurableTypeAction action, String category) {
        this.uniqueInstance = uniqueInstance;
        this.configClass = configClass;
        this.action = action;
        this.category = category;
    }

    /**
     * If this type should refer to a configurable with a unique instance.
     * If this is true, the configurable should have a public static void
     * initInstance(ExtendedConfig eConfig) method and also a public static
     * (? extends IConfigurable) getInstance() method.
     * @return If it has a unique instance.
     */
    public boolean hasUniqueInstance() {
        return uniqueInstance;
    }

    /**
     * Get the class that extends {@link ExtendedConfig} this type can hold.
     * @return The class that extends {@link ExtendedConfig} this type can hold.
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends ExtendedConfig> getConfigClass() {
        return configClass;
    }

    /**
     * The action for this type after the the configurable has configured so it can be registered.
     * @return The action for this type.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableTypeAction getConfigurableTypeAction() {
        return action;
    }

    public void setAction(ConfigurableTypeAction action) {
        this.action = action;
    }

    /**
     * The category of this type.
     * @return The category.
     */
    public String getCategory() {
        return category;
    }
}
