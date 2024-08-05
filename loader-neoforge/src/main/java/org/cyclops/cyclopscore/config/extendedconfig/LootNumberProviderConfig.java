package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot number providers.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class LootNumberProviderConfig extends LootNumberProviderConfigCommon<ModBase<?>> {
    public LootNumberProviderConfig(ModBase<?> mod, String namedId, LootNumberProviderType lootItemFunctionType) {
        super(mod, namedId, lootItemFunctionType);
    }
}
