package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public interface IInventoryHelpers {

    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param inventory inventory with ItemStacks
     * @param blockPos The position.
     */
    public void dropItems(Level world, Container inventory, BlockPos blockPos);


    /**
     * Erase a complete inventory
     * @param inventory inventory to clear
     */
    public void clearInventory(Container inventory);

    /**
     * Try adding a new item stack originating from the given original stack to the same original stack.
     * The original item stack should not have it's stack-size decreased yet, this method does this.
     * Otherwise it will add the new stack to another inventory slot and in the worst case drop it on the floor.
     * @param player The player.
     * @param originalStack The original item stack from which the new item stack originated.
     * @param newStackPart The new item stack.
     * @param hand The hand for which the stack should be re-added
     */
    public void tryReAddToStack(Player player, ItemStack originalStack, ItemStack newStackPart, InteractionHand hand);

    /**
     * Read an inventory from NBT.
     * @param inventory The inventory.
     * @param data The tag to read from.
     * @param tagName The tag name to read from.
     */
    public void readFromNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName);

    /**
     * Write the given inventory to NBT.
     * @param inventory The inventory.
     * @param data The tag to write to.
     * @param tagName The tag name to write into.
     */
    public void writeToNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName);

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToSlot(Container inventory, int slot, ItemStack itemStack);

    /**
     * Tries to merge the given stacks.
     * @param itemStack The stack to mutate and add to.
     * @param toAdd The stack to add.
     * @return The remainder of the added stack
     */
    public ItemStack addToStack(ItemStack itemStack, ItemStack toAdd);

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @param simulate If the operation should be simulated.
     * @return The remaining itemstack that could not be added anymore.
     */
    public ItemStack fillSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate);

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @param simulate If the operation should be simulated.
     * @return If the item could be added or joined in the production slot.
     */
    public boolean addToSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate);

    /**
     * Try to add the given item to any of the given slots.
     * @param inventory The inventory.
     * @param slots The slots
     * @param itemStacks The items to try to put in the inventory.
     * @param simulate If the operation should be simulated.
     * @return The remaining itemstack that could not be added anymore.
     */
    public NonNullList<ItemStack> addToInventory(Container inventory, int[] slots, NonNullList<ItemStack> itemStacks, boolean simulate);

    /**
     * Add the given stack to the given list, by attempting
     * to increase the stacksize of equal stacks that are already present.
     * @param itemStacks The list of stacks.
     * @param itemStack The stack to add to the list.
     */
    public void addStackToList(NonNullList<ItemStack> itemStacks, ItemStack itemStack);

}
