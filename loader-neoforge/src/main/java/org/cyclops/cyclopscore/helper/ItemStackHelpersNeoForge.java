package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class ItemStackHelpersNeoForge extends ItemStackHelpersCommon {
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainder().create();
    }

    @Override
    protected boolean hasComponentPatch(ItemStack stack) {
        // Answers without building the patch instance that the common implementation needs
        return !stack.isComponentsPatchEmpty();
    }
}
