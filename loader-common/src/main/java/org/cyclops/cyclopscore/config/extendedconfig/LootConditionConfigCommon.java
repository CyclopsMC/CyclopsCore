package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot functions.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootConditionConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootConditionConfigCommon<M>, MapCodec<? extends LootItemCondition>, M> {

    public LootConditionConfigCommon(M mod, String namedId, MapCodec<? extends LootItemCondition> lootItemFunctionType) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_CONDITION;
    }

    @Override
    public Registry<MapCodec<? extends LootItemCondition>> getRegistry() {
        return BuiltInRegistries.LOOT_CONDITION_TYPE;
    }
}
