package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot number providers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class LootNumberProviderConfig extends ExtendedConfigForge<LootNumberProviderConfig, LootNumberProviderType> {
    public LootNumberProviderConfig(ModBase<?> mod, String namedId, LootNumberProviderType lootItemFunctionType) {
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
        return ConfigurableTypesNeoForge.D_LOOT_NUMBER_PROVIDER;
    }

    @Override
    public Registry<LootNumberProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE;
    }
}
