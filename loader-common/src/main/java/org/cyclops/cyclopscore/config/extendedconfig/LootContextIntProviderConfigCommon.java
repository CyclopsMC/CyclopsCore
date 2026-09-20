package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot context int providers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class LootContextIntProviderConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<LootContextIntProviderConfigCommon<M>, MapCodec<? extends ContextIntProvider>, M> {

    public LootContextIntProviderConfigCommon(M mod, String namedId, MapCodec<? extends ContextIntProvider> lootContextIntProviderType) {
        super(mod, namedId, (eConfig) -> lootContextIntProviderType);
    }

    @Override
    public String getTranslationKey() {
        return "lootcontextintprovider." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.LOOT_CONTEXT_INT_PROVIDER;
    }

    @Override
    public Registry<MapCodec<? extends ContextIntProvider>> getRegistry() {
        return BuiltInRegistries.CONTEXT_INT_PROVIDER_TYPE;
    }
}
