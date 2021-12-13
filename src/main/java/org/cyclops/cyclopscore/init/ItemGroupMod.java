package org.cyclops.cyclopscore.init;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

/**
 * A creative tab which displays a simple item or block with the modname as label.
 * @author rubensworks
 */
public class ItemGroupMod extends ItemGroup {

    private final Supplier<Item> item;

    public ItemGroupMod(ModBase mod, Supplier<Item> item) {
        super(mod.getModId());
        this.item = item;
    }

    @Override
    public ItemStack makeIcon() {
        Item i = item.get();
        if (i == null) {
            i = Item.byBlock(Blocks.BARRIER);
        }
        return new ItemStack(i);
    }
}
