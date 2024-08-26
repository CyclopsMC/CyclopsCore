package org.cyclops.cyclopscore.config;

import org.cyclops.cyclopscore.config.configurabletypeaction.BlockAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.CapabilityAction;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionCommon;
import org.cyclops.cyclopscore.config.configurabletypeaction.ConfigurableTypeActionRegistry;
import org.cyclops.cyclopscore.config.configurabletypeaction.DummyActionCommon;
import org.cyclops.cyclopscore.config.configurabletypeaction.ItemAction;
import org.cyclops.cyclopscore.config.extendedconfig.*;

/**
 * The different types of configurable.
 * @author rubensworks
 *
 */
public class ConfigurableTypeCommon {

    public static final ConfigurableTypeCommon ITEM = new ConfigurableTypeCommon(true, ItemConfigCommon.class, new ItemAction<>(), "item");
    public static final ConfigurableTypeCommon BLOCK = new ConfigurableTypeCommon(true, BlockConfigCommon.class, new BlockAction<>(), "block");
    public static final ConfigurableTypeCommon VILLAGER = new ConfigurableTypeCommon(true, VillagerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "mob");
    public static final ConfigurableTypeCommon EFFECT = new ConfigurableTypeCommon(true, EffectConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "potion");
    public static final ConfigurableTypeCommon CAPABILITY = new ConfigurableTypeCommon(false, CapabilityConfigCommon.class, new CapabilityAction<>(), "capability");
    public static final ConfigurableTypeCommon RECIPE = new ConfigurableTypeCommon(true, RecipeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "recipe");
    public static final ConfigurableTypeCommon RECIPE_TYPE = new ConfigurableTypeCommon(true, RecipeTypeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "recipe_type");
    public static final ConfigurableTypeCommon BLOCK_ENTITY = new ConfigurableTypeCommon(true, BlockEntityConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "block_entity");
    public static final ConfigurableTypeCommon WORLD_FEATURE = new ConfigurableTypeCommon(true, WorldFeatureConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_feature");
    public static final ConfigurableTypeCommon WORLD_STRUCTURE = new ConfigurableTypeCommon(true, WorldStructureConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_structure");
    public static final ConfigurableTypeCommon WORLD_STRUCTURE_PIECE = new ConfigurableTypeCommon(true, WorldStructurePieceConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "world_structure_piece");
    public static final ConfigurableTypeCommon FOLIAGE_PLACER = new ConfigurableTypeCommon(true, FoliagePlacerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "foliage_placer");
    public static final ConfigurableTypeCommon TRUNK_PLACER = new ConfigurableTypeCommon(true, TrunkPlacerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "trunk_placer");
    public static final ConfigurableTypeCommon ARGUMENT_TYPE = new ConfigurableTypeCommon(true, ArgumentTypeConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "argument_type");
    public static final ConfigurableTypeCommon CREATIVE_MODE_TAB = new ConfigurableTypeCommon(true, CreativeModeTabConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "creative_mode_tab");
    public static final ConfigurableTypeCommon CRITERION_TRIGGER = new ConfigurableTypeCommon(true, CriterionTriggerConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "criterion_trigger");
    public static final ConfigurableTypeCommon LOOT_FUNCTION = new ConfigurableTypeCommon(true, LootFunctionConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_function");
    public static final ConfigurableTypeCommon LOOT_CONDITION = new ConfigurableTypeCommon(true, LootConditionConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_condition");
    public static final ConfigurableTypeCommon LOOT_NUMBER_PROVIDER = new ConfigurableTypeCommon(true, LootNumberProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_number_provider");
    public static final ConfigurableTypeCommon LOOT_NBT_PROVIDER = new ConfigurableTypeCommon(true, LootNbtProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_nbt_provider");
    public static final ConfigurableTypeCommon LOOT_SCORE_PROVIDER = new ConfigurableTypeCommon(true, LootScoreProviderConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "loot_score_provider");
    public static final ConfigurableTypeCommon SOUND_EVENT = new ConfigurableTypeCommon(true, SoundEventConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "sound_event");
    public static final ConfigurableTypeCommon DATA_COMPONENT = new ConfigurableTypeCommon(true, DataComponentConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "data_component");
    public static final ConfigurableTypeCommon ARMOR_MATERIAL = new ConfigurableTypeCommon(true, ArmorMaterialConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "armor_material");
    public static final ConfigurableTypeCommon ENCHANTMENT_ENTITY_EFFECT = new ConfigurableTypeCommon(true, EnchantmentEntityEffectConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "enchantment_entity_effect");
    public static final ConfigurableTypeCommon GUI = new ConfigurableTypeCommon(true, GuiConfigCommon.class, new ConfigurableTypeActionRegistry<>(), "gui");

    /**
     * Dummy type, only used for configs that refer to nothing.
     */
    public static final ConfigurableTypeCommon DUMMY = new ConfigurableTypeCommon(false, DummyConfigCommon.class, new DummyActionCommon<>(), "general");

    private final boolean uniqueInstance;
    @SuppressWarnings("rawtypes")
    private final Class<? extends ExtendedConfigCommon> configClass;
    @SuppressWarnings("rawtypes")
    private ConfigurableTypeActionCommon action;
    private final String category;

    /**
     * Make a new instance.
     * @param uniqueInstance If this type has a unique instance for each config.
     * @param configClass The config class.
     * @param action The action instance that helps with saving of the config and optional instance.
     * @param category The category in which the configs should be saved.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableTypeCommon(boolean uniqueInstance, Class<? extends ExtendedConfigCommon> configClass,
                                  ConfigurableTypeActionCommon action, String category) {
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
     * Get the class that extends {@link ExtendedConfigCommon} this type can hold.
     * @return The class that extends {@link ExtendedConfigCommon} this type can hold.
     */
    @SuppressWarnings("rawtypes")
    public Class<? extends ExtendedConfigCommon> getConfigClass() {
        return configClass;
    }

    /**
     * The action for this type after the the configurable has configured so it can be registered.
     * @return The action for this type.
     */
    @SuppressWarnings("rawtypes")
    public ConfigurableTypeActionCommon getConfigurableTypeAction() {
        return action;
    }

    public void setAction(ConfigurableTypeActionCommon action) {
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
