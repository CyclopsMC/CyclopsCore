package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot functions.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class LootConditionConfig extends LootConditionConfigCommon<ModBase<?>> {
    public LootConditionConfig(ModBase<?> mod, String namedId, LootItemConditionType lootItemFunctionType) {
        super(mod, namedId, lootItemFunctionType);
    }
}
