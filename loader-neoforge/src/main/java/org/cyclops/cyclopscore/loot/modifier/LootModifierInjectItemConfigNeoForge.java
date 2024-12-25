package org.cyclops.cyclopscore.loot.modifier;

import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigNeoForge;

/**
 * @author rubensworks
 */
public class LootModifierInjectItemConfigNeoForge extends LootModifierConfigNeoForge<LootModifierInjectItem> {
    public LootModifierInjectItemConfigNeoForge() {
        super(CyclopsCoreNeoForge._instance, "inject_item", (eConfig) -> LootModifierInjectItem.CODEC.get());
    }
}
