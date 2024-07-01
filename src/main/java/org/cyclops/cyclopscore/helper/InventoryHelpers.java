package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Contains helper methods involving {@link Container}S.
 * @author immortaleeb
 *
 */
public class InventoryHelpers {

    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param inventory inventory with ItemStacks
     * @param blockPos The position.
     */
    public static void dropItems(Level world, Container inventory, BlockPos blockPos) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0)
                ItemStackHelpers.spawnItemStack(world, blockPos, inventory.getItem(i).copy());
        }
    }

    /**
     * Erase a complete inventory
     * @param inventory inventory to clear
     */
    public static void clearInventory(Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }
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
        if (!player.isCreative()) {
            if(!originalStack.isEmpty() && originalStack.getCount() == 1) {
                player.getInventory().setItem(hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40, newStackPart);
            } else {
                if(!originalStack.isEmpty()) {
                    originalStack.shrink(1);
                }
                if(!player.getInventory().add(newStackPart)) {
                    player.drop(newStackPart, false);
                }
            }
        }
    }

    /**
     * Read an inventory from NBT.
     * @param inventory The inventory.
     * @param data The tag to read from.
     * @param tagName The tag name to read from.
     */
    public static void readFromNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
        ListTag nbttaglist = data.getList(tagName, Tag.TAG_COMPOUND);

        for(int j = 0; j < inventory.getContainerSize(); j++) {
            inventory.setItem(j, ItemStack.EMPTY);
        }

        for(int j = 0; j < nbttaglist.size(); j++) {
            CompoundTag slot = nbttaglist.getCompound(j);
            int index;
            if(slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getByte("Slot");
            }
            if(index >= 0 && index < inventory.getContainerSize()) {
                inventory.setItem(index, ItemStack.parseOptional(provider, slot));
            }
        }
    }

    /**
     * Write the given inventory to NBT.
     * @param inventory The inventory.
     * @param data The tag to write to.
     * @param tagName The tag name to write into.
     */
    public static void writeToNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
        ListTag slots = new ListTag();
        for(byte index = 0; index < inventory.getContainerSize(); ++index) {
            ItemStack itemStack = inventory.getItem(index);
            if(!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundTag slot = new CompoundTag();
                slot.putInt("index", index);
                slots.add(slot);
                itemStack.save(provider, slot);
            }
        }
        data.put(tagName, slots);
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
        return addToSlot(inventory, slot, itemStack, false);
    }

    /**
     * Tries to merge the given stacks.
     * @param itemStack The stack to mutate and add to.
     * @param toAdd The stack to add.
     * @return The remainder of the added stack
     */
    public static ItemStack addToStack(ItemStack itemStack, ItemStack toAdd) {
        if (ItemStack.isSameItemSameComponents(toAdd, itemStack)
                && itemStack.getCount() < itemStack.getMaxStackSize()) {
            toAdd = toAdd.copy();
            int toAddCount = Math.min(itemStack.getMaxStackSize() - itemStack.getCount(), toAdd.getCount());
            itemStack.grow(toAddCount);
            toAdd.shrink(toAddCount);
        }
        return toAdd;
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
        ItemStack produceStack = inventory.getItem(slot);
        if(produceStack.isEmpty()) {
            if (!simulate) {
                inventory.setItem(slot, itemStack);
            }
            return ItemStack.EMPTY;
        } else {
            produceStack = produceStack.copy();
            ItemStack remainder = addToStack(produceStack, itemStack);
            if (!simulate && remainder.getCount() != itemStack.getCount()) {
                inventory.setItem(slot, produceStack);
            }
            return remainder;
        }
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
        return fillSlot(inventory, slot, itemStack, simulate).isEmpty();
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
        NonNullList<ItemStack> remaining = NonNullList.create();
        for (ItemStack itemStack : itemStacks) {
            for (int i = 0; i < slots.length; i++) {
                int slot = slots[i];
                itemStack = fillSlot(inventory, slot, itemStack, simulate);
                if (simulate) {
                    // We blacklist this slot in the next iteration,
                    // because in simulation, we don't fill the actual slot,
                    // so it could occur that only one slot is empty,
                    // and two different item outputs of this recipe want to fill that slot.
                    // Note: this is a heuristic, and may be to pessimistic in some cases.
                    slots = ArrayUtils.remove(slots, i);
                    i--;
                }
                if (itemStack.isEmpty()) {
                    break;
                }
            }
            if (!itemStack.isEmpty()) {
                remaining.add(itemStack);
            }
        }
        return remaining;
    }

    /**
     * Add the given stack to the given list, by attempting
     * to increase the stacksize of equal stacks that are already present.
     * @param itemStacks The list of stacks.
     * @param itemStack The stack to add to the list.
     */
    public static void addStackToList(NonNullList<ItemStack> itemStacks, ItemStack itemStack) {
        // Try to add the stack to one of the already-present stacks before adding a new element
        boolean added = false;
        ItemStack toAdd = itemStack;
        for (ItemStack existingOutputStack: itemStacks) {
            toAdd = InventoryHelpers.addToStack(existingOutputStack, toAdd);
            if (toAdd.isEmpty()) {
                added = true;
                break;
            }
        }
        if (!added) {
            itemStacks.add(toAdd.copy());
        }
    }

}
