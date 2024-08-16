package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot number providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public abstract class LootNumberProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootNumberProviderConfigCommon<M>, LootNumberProviderType, M> {

    public LootNumberProviderConfigCommon(M mod, String namedId, LootNumberProviderType lootItemFunctionType) {
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
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.LOOT_NUMBER_PROVIDER;
    }

    @Override
    public Registry<LootNumberProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE;
    }
}
