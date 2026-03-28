package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot functions.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootFunctionConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootFunctionConfigCommon<M>, MapCodec<? extends LootItemFunction>, M> {

    public LootFunctionConfigCommon(M mod, String namedId, MapCodec<? extends LootItemFunction> lootItemFunctionType) {
        super(mod, namedId, (eConfig) -> lootItemFunctionType);
    }

    @Override
    public String getTranslationKey() {
        return "lootfunction." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_FUNCTION;
    }

    @Override
    public Registry<MapCodec<? extends LootItemFunction>> getRegistry() {
        return BuiltInRegistries.LOOT_FUNCTION_TYPE;
    }
}
