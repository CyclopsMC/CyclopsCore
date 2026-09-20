package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot context float providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootContextFloatProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootContextFloatProviderConfigCommon<M>, MapCodec<? extends ContextFloatProvider>, M> {

    public LootContextFloatProviderConfigCommon(M mod, String namedId, MapCodec<? extends ContextFloatProvider> lootContextFloatProviderType) {
        super(mod, namedId, (eConfig) -> lootContextFloatProviderType);
    }

    @Override
    public String getTranslationKey() {
        return "lootcontextfloatprovider." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_CONTEXT_FLOAT_PROVIDER;
    }

    @Override
    public Registry<MapCodec<? extends ContextFloatProvider>> getRegistry() {
        return BuiltInRegistries.CONTEXT_FLOAT_PROVIDER_TYPE;
    }
}
