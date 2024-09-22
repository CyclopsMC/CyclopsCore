package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class ItemStackHelpersFabric extends ItemStackHelpersCommon {
    @Override
    public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getItem().hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getRecipeRemainder();
    }
}
