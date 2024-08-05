package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for loot functions.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class LootFunctionConfig extends LootFunctionConfigCommon<ModBase<?>> {
    public LootFunctionConfig(ModBase<?> mod, String namedId, LootItemFunctionType<?> lootItemFunctionType) {
        super(mod, namedId, lootItemFunctionType);
    }
}
