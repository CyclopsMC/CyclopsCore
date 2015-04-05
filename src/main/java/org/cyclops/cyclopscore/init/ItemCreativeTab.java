package org.cyclops.cyclopscore.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * A creative tab which displays a simple item or block with the modname as label.
 * @author rubensworks
 */
public class ItemCreativeTab extends CreativeTabs {

    private Item item;

    public ItemCreativeTab(ModBase mod, Item item) {
        super(mod.getModId());
        this.item = item;
    }

    public ItemCreativeTab(ModBase mod, Block block) {
        this(mod, Item.getItemFromBlock(block));
    }

    @Override
    public Item getTabIconItem() {
        return item;
    }
}
