package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Contains helper methods involving {@link net.minecraft.inventory.IInventory}S.
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
	public static void dropItems(World world, IInventory inventory, BlockPos blockPos) {
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
	public static void clearInventory(IInventory inventory) {
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
	public static void tryReAddToStack(PlayerEntity player, ItemStack originalStack, ItemStack newStackPart) {
		tryReAddToStack(player, originalStack, newStackPart, Hand.MAIN_HAND);
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
	public static void tryReAddToStack(PlayerEntity player, ItemStack originalStack, ItemStack newStackPart, Hand hand) {
		if (!player.isCreative()) {
        	if(!originalStack.isEmpty() && originalStack.getCount() == 1) {
        		player.inventory.setItem(hand == Hand.MAIN_HAND ? player.inventory.selected : 40, newStackPart);
        	} else {
                if(!originalStack.isEmpty()) {
					originalStack.shrink(1);
				}
        		if(!player.inventory.add(newStackPart)) {
        			player.drop(newStackPart, false);
        		}
        	}
        }
	}
	
	/**
	 * Validate the NBT storage of the given inventory in the given item.
	 * Should be called in constructors of inventories.
	 * @param inventory The inventory.
	 * @param itemStack The item stack to read/write.
	 * @param tagName The tag name to read from.
	 */
	public static void validateNBTStorage(IInventory inventory, ItemStack itemStack, String tagName) {
		CompoundNBT tag = itemStack.getOrCreateTag();
		if(!tag.contains(tagName)) {
			tag.put(tagName, new ListNBT());
		}
		readFromNBT(inventory, tag, tagName);
	}
	
	/**
	 * Read an inventory from NBT.
	 * @param inventory The inventory.
	 * @param data The tag to read from.
	 * @param tagName The tag name to read from.
	 */
	public static void readFromNBT(IInventory inventory, CompoundNBT data, String tagName) {
        ListNBT nbttaglist = data.getList(tagName, Constants.NBT.TAG_COMPOUND);
        
        for(int j = 0; j < inventory.getContainerSize(); j++) {
        	inventory.setItem(j, ItemStack.EMPTY);
        }

        for(int j = 0; j < nbttaglist.size(); j++) {
            CompoundNBT slot = nbttaglist.getCompound(j);
            int index;
            if(slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getByte("Slot");
            }
            if(index >= 0 && index < inventory.getContainerSize()) {
            	inventory.setItem(index, ItemStack.of(slot));
            }
        }
    }
	
	/**
	 * Write the given inventory to NBT.
	 * @param inventory The inventory.
	 * @param data The tag to write to.
	 * @param tagName The tag name to write into.
	 */
	public static void writeToNBT(IInventory inventory, CompoundNBT data, String tagName) {
        ListNBT slots = new ListNBT();
        for(byte index = 0; index < inventory.getContainerSize(); ++index) {
        	ItemStack itemStack = inventory.getItem(index);
            if(!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundNBT slot = new CompoundNBT();
                slot.putInt("index", index);
                slots.add(slot);
                itemStack.save(slot);
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
	public static ItemStack getItemFromIndex(PlayerEntity player, int itemIndex) {
		return getItemFromIndex(player, itemIndex, Hand.MAIN_HAND);
	}
	
	/**
	 * Get the item stack from the given index in the player inventory.
	 * @param player The player.
	 * @param itemIndex The index of the item in the inventory.
	 * @param hand The hand the item is in.
	 * @return The item stack.
	 */
	public static ItemStack getItemFromIndex(PlayerEntity player, int itemIndex, Hand hand) {
		return Hand.MAIN_HAND.equals(hand)
				? player.inventory.items.get(itemIndex) : player.getOffhandItem();
	}

	/**
	 * Set the item stack at the given index in the player inventory.
	 * @param player The player.
	 * @param itemIndex The index of the item in the inventory.
	 * @param hand The hand the item is in.
	 * @param itemStack The new item stack.
	 */
	public static void setItemAtIndex(PlayerEntity player, int itemIndex, Hand hand, ItemStack itemStack) {
		if (Hand.MAIN_HAND.equals(hand)) {
			player.inventory.setItem(itemIndex, itemStack);
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
	public static boolean addToSlot(IInventory inventory, int slot, ItemStack itemStack) {
		return addToSlot(inventory, slot, itemStack, false);
	}

	/**
	 * Tries to merge the given stacks.
	 * @param itemStack The stack to mutate and add to.
	 * @param toAdd The stack to add.
	 * @return The remainder of the added stack
	 */
	public static ItemStack addToStack(ItemStack itemStack, ItemStack toAdd) {
		if (ItemStack.tagMatches(toAdd, itemStack)
				&& ItemStack.isSame(toAdd, itemStack)
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
	public static ItemStack fillSlot(IInventory inventory, int slot, ItemStack itemStack, boolean simulate) {
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
    public static boolean addToSlot(IInventory inventory, int slot, ItemStack itemStack, boolean simulate) {
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
	public static NonNullList<ItemStack> addToInventory(IInventory inventory, int[] slots, NonNullList<ItemStack> itemStacks, boolean simulate) {
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
