package org.cyclops.cyclopscore.loot.modifier;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;

/**
 * @author rubensworks
 */
public class LootModifierInjectItemConfig extends LootModifierConfig<LootModifierInjectItem> {
    public LootModifierInjectItemConfig() {
        super(CyclopsCore._instance, "inject_item", (eConfig) -> LootModifierInjectItem.CODEC.get());
    }
}
