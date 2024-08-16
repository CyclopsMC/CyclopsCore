package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot nbt providers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class LootNbtProviderConfig extends LootNbtProviderConfigCommon<ModBase<?>> {
    public LootNbtProviderConfig(ModBase<?> mod, String namedId, LootNbtProviderType lootItemFunctionType) {
        super(mod, namedId, lootItemFunctionType);
    }
}
