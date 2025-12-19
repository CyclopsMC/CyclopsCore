package org.cyclops.cyclopscore.inventory;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

/**
 * A dummy item implementation.
 * @author rubensworks
 */
public class ItemDummy extends Item {

    public ItemDummy() {
        super(new Properties().setId(ResourceKey.create(ResourceKey.createRegistryKey(Registries.ITEM.registry()), Identifier.fromNamespaceAndPath("cyclopscore", "dummy"))));
    }

}
