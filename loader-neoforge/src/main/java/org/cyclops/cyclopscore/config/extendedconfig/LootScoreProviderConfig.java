package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot score providers.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class LootScoreProviderConfig extends LootScoreProviderConfigCommon<ModBase<?>> {
    public LootScoreProviderConfig(ModBase<?> mod, String namedId, LootScoreProviderType lootItemFunctionType) {
        super(mod, namedId, lootItemFunctionType);
    }
}
