package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class ItemStackHelpersNeoForge extends ItemStackHelpersCommon {
    @Override
    public boolean hasCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainingItem();
    }
}
