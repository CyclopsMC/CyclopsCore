package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for loot nbt providers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class LootNbtProviderConfigCommon<M extends IModBase> extends ExtendedConfigForge<LootNbtProviderConfigCommon<M>, LootNbtProviderType, M> {

    public LootNbtProviderConfigCommon(M mod, String namedId, LootNbtProviderType lootItemFunctionType) {
        super(mod, namedId, (eConfig) -> lootItemFunctionType);
    }

    @Override
    public String getTranslationKey() {
        return "lootnbtprovider." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.LOOT_NBT_PROVIDER;
    }

    @Override
    public Registry<LootNbtProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE;
    }
}
