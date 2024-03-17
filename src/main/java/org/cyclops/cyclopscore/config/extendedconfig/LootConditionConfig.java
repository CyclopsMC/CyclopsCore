package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot functions.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class LootConditionConfig extends ExtendedConfigForge<LootConditionConfig, LootItemConditionType> {

    public LootConditionConfig(ModBase mod, String namedId, LootItemConditionType lootItemFunctionType) {
        super(mod, namedId, (eConfig) -> lootItemFunctionType);
    }

    @Override
    public String getTranslationKey() {
        return "lootcondition." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.LOOT_CONDITION;
    }

    @Override
    public Registry<LootItemConditionType> getRegistry() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE;
    }
}
