package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot score providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootScoreProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootScoreProviderConfigCommon<M>, MapCodec<? extends ScoreboardNameProvider>, M> {

    public LootScoreProviderConfigCommon(M mod, String namedId, MapCodec<? extends ScoreboardNameProvider> lootItemFunctionType) {
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
    public Registry<MapCodec<? extends ScoreboardNameProvider>> getRegistry() {
        return BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE;
    }
}
