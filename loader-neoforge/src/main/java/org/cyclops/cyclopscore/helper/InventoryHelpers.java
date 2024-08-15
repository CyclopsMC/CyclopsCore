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
 * Contains helper methods involving {@link Container}S.
 * @author immortaleeb
 *
 */
@Deprecated // TODO: remove in next major version
public class InventoryHelpers {

    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param inventory inventory with ItemStacks
     * @param blockPos The position.
     */
    public static void dropItems(Level world, Container inventory, BlockPos blockPos) {
        IModHelpers.get().getInventoryHelpers().dropItems(world, inventory, blockPos);
    }

    /**
     * Erase a complete inventory
     * @param inventory inventory to clear
     */
    public static void clearInventory(Container inventory) {
        IModHelpers.get().getInventoryHelpers().clearInventory(inventory);
    }

    /**
     * Try adding a new item stack originating from the given original stack to the same original stack.
     * The original item stack should not have it's stack-size decreased yet, this method does this.
     * Otherwise it will add the new stack to another inventory slot and in the worst case drop it on the floor.
     * @param player The player.
     * @param originalStack The original item stack from which the new item stack originated.
     * @param newStackPart The new item stack.
     */
    @Deprecated
    public static void tryReAddToStack(Player player, ItemStack originalStack, ItemStack newStackPart) {
        tryReAddToStack(player, originalStack, newStackPart, InteractionHand.MAIN_HAND);
    }

    /**
     * Try adding a new item stack originating from the given original stack to the same original stack.
     * The original item stack should not have it's stack-size decreased yet, this method does this.
     * Otherwise it will add the new stack to another inventory slot and in the worst case drop it on the floor.
     * @param player The player.
     * @param originalStack The original item stack from which the new item stack originated.
     * @param newStackPart The new item stack.
     * @param hand The hand for which the stack should be re-added
     */
    public static void tryReAddToStack(Player player, ItemStack originalStack, ItemStack newStackPart, InteractionHand hand) {
        IModHelpers.get().getInventoryHelpers().tryReAddToStack(player, originalStack, newStackPart, hand);
    }

    /**
     * Read an inventory from NBT.
     * @param inventory The inventory.
     * @param data The tag to read from.
     * @param tagName The tag name to read from.
     */
    public static void readFromNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
        IModHelpers.get().getInventoryHelpers().readFromNBT(provider, inventory, data, tagName);
    }

    /**
     * Write the given inventory to NBT.
     * @param inventory The inventory.
     * @param data The tag to write to.
     * @param tagName The tag name to write into.
     */
    public static void writeToNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
        IModHelpers.get().getInventoryHelpers().writeToNBT(provider, inventory, data, tagName);
    }

    /**
     * Get the item stack from the given index in the player inventory.
     * @param player The player.
     * @param itemIndex The index of the item in the inventory.
     * @return The item stack.
     */
    @Deprecated  // TODO: rm in 1.19
    public static ItemStack getItemFromIndex(Player player, int itemIndex) {
        return getItemFromIndex(player, itemIndex, InteractionHand.MAIN_HAND);
    }

    /**
     * Get the item stack from the given index in the player inventory.
     * @param player The player.
     * @param itemIndex The index of the item in the inventory.
     * @param hand The hand the item is in.
     * @return The item stack.
     * @deprecated Use IInventoryLocation
     */
    @Deprecated // TODO: rm in 1.19
    public static ItemStack getItemFromIndex(Player player, int itemIndex, InteractionHand hand) {
        return InteractionHand.MAIN_HAND.equals(hand)
                ? player.getInventory().items.get(itemIndex) : player.getOffhandItem();
    }

    /**
     * Set the item stack at the given index in the player inventory.
     * @param player The player.
     * @param itemIndex The index of the item in the inventory.
     * @param hand The hand the item is in.
     * @param itemStack The new item stack.
     *  @deprecated Use IInventoryLocation
     */
    @Deprecated  // TODO: rm in 1.19
    public static void setItemAtIndex(Player player, int itemIndex, InteractionHand hand, ItemStack itemStack) {
        if (InteractionHand.MAIN_HAND.equals(hand)) {
            player.getInventory().setItem(itemIndex, itemStack);
        } else {
            player.setItemInHand(hand, itemStack);
        }
    }

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @return If the item could be added or joined in the production slot.
     */
    public static boolean addToSlot(Container inventory, int slot, ItemStack itemStack) {
        return IModHelpers.get().getInventoryHelpers().addToSlot(inventory, slot, itemStack);
    }

    /**
     * Tries to merge the given stacks.
     * @param itemStack The stack to mutate and add to.
     * @param toAdd The stack to add.
     * @return The remainder of the added stack
     */
    public static ItemStack addToStack(ItemStack itemStack, ItemStack toAdd) {
        return IModHelpers.get().getInventoryHelpers().addToStack(itemStack, toAdd);
    }

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @param simulate If the operation should be simulated.
     * @return The remaining itemstack that could not be added anymore.
     */
    public static ItemStack fillSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate) {
        return IModHelpers.get().getInventoryHelpers().fillSlot(inventory, slot, itemStack, simulate);
    }

    /**
     * Try to add the given item to the given slot.
     * @param inventory The inventory.
     * @param slot The slot to add to.
     * @param itemStack The item to try to put in the production slot.
     * @param simulate If the operation should be simulated.
     * @return If the item could be added or joined in the production slot.
     */
    public static boolean addToSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate) {
        return IModHelpers.get().getInventoryHelpers().addToSlot(inventory, slot, itemStack, simulate);
    }

    /**
     * Try to add the given item to any of the given slots.
     * @param inventory The inventory.
     * @param slots The slots
     * @param itemStacks The items to try to put in the inventory.
     * @param simulate If the operation should be simulated.
     * @return The remaining itemstack that could not be added anymore.
     */
    public static NonNullList<ItemStack> addToInventory(Container inventory, int[] slots, NonNullList<ItemStack> itemStacks, boolean simulate) {
        return IModHelpers.get().getInventoryHelpers().addToInventory(inventory, slots, itemStacks, simulate);
    }

    /**
     * Add the given stack to the given list, by attempting
     * to increase the stacksize of equal stacks that are already present.
     * @param itemStacks The list of stacks.
     * @param itemStack The stack to add to the list.
     */
    public static void addStackToList(NonNullList<ItemStack> itemStacks, ItemStack itemStack) {
        IModHelpers.get().getInventoryHelpers().addStackToList(itemStacks, itemStack);
    }

}
