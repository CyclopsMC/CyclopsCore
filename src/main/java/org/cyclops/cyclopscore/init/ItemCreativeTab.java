package org.cyclops.cyclopscore.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * A creative tab which displays a simple item or block with the modname as label.
 * @author rubensworks
 */
public class ItemCreativeTab extends CreativeTabs {

    private final IObjectReference<Item> item;

    public ItemCreativeTab(ModBase mod, IObjectReference<Item> item) {
        super(mod.getModId());
        this.item = item;
    }

    @Override
    public Item getTabIconItem() {
        return item.getObject();
    }
}
