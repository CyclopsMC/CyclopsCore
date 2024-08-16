package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot nbt providers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class LootNbtProviderConfig extends ExtendedConfigForge<LootNbtProviderConfig, LootNbtProviderType> {
    public LootNbtProviderConfig(ModBase<?> mod, String namedId, LootNbtProviderType lootItemFunctionType) {
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
        return ConfigurableTypesNeoForge.D_LOOT_NBT_PROVIDER;
    }

    @Override
    public Registry<LootNbtProviderType> getRegistry() {
        return BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE;
    }
}
