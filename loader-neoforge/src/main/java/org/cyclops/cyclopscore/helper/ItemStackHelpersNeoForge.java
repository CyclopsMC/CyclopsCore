package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class ItemStackHelpersNeoForge extends ItemStackHelpersCommon {
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainder();
    }
}
