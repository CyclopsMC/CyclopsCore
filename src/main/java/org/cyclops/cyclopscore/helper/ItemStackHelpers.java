package org.cyclops.cyclopscore.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Contains helper methods for various itemstack specific things.
 * @author rubensworks
 */
public final class ItemStackHelpers {

    /**
     * Get the tag compound from an item safely.
     * If it does not exist yet, it will create and save a new tag compound.
     * @param itemStack The item to get the tag compound from.
     * @return The tag compound.
     */
    public static NBTTagCompound getSafeTagCompound(ItemStack itemStack) {
        if(!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }

}
