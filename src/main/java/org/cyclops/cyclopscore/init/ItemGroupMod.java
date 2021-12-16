package org.cyclops.cyclopscore.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

/**
 * A creative tab which displays a simple item or block with the modname as label.
 * @author rubensworks
 */
public class ItemGroupMod extends CreativeModeTab {

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
