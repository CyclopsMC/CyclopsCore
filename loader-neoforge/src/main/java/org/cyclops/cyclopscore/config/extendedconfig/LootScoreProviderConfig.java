package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot score providers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class LootScoreProviderConfig extends ExtendedConfigForge<LootScoreProviderConfig, LootScoreProviderType> {
    public LootScoreProviderConfig(ModBase<?> mod, String namedId, LootScoreProviderType lootItemFunctionType) {
        super(mod, namedId, (eConfig) -> lootItemFunctionType);
    }

    @Override
    public String getTranslationKey() {
        return "lootscoreprovider." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_LOOT_SCORE_PROVIDER;
    }

    @Override
    public Registry<LootScoreProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE;
    }
}
