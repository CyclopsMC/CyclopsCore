package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot number providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootNumberProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootNumberProviderConfigCommon<M>, MapCodec<? extends NumberProvider>, M> {

    public LootNumberProviderConfigCommon(M mod, String namedId, MapCodec<? extends NumberProvider> lootItemFunctionType) {
        super(mod, namedId, (eConfig) -> lootItemFunctionType);
    }

    @Override
    public String getTranslationKey() {
        return "lootnumberprovider." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_NUMBER_PROVIDER;
    }

    @Override
    public Registry<MapCodec<? extends NumberProvider>> getRegistry() {
        return BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE;
    }
}
