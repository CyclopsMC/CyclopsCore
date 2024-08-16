package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot score providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootScoreProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootScoreProviderConfigCommon<M>, LootScoreProviderType, M> {

    public LootScoreProviderConfigCommon(M mod, String namedId, LootScoreProviderType lootItemFunctionType) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_SCORE_PROVIDER;
    }

    @Override
    public Registry<LootScoreProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE;
    }
}
