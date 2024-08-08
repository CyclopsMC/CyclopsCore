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
 * @author rubensworks
 */
public class InventoryHelpersCommon implements IInventoryHelpers {

    private final IModHelpers modHelpers;

    public InventoryHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public void dropItems(Level world, Container inventory, BlockPos blockPos) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0)
                this.modHelpers.getItemStackHelpers().spawnItemStack(world, blockPos, inventory.getItem(i).copy());
        }
    }

    @Override
    public void clearInventory(Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void tryReAddToStack(Player player, ItemStack originalStack, ItemStack newStackPart, InteractionHand hand) {
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

    @Override
    public void readFromNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
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

    @Override
    public void writeToNBT(HolderLookup.Provider provider, Container inventory, CompoundTag data, String tagName) {
        ListTag slots = new ListTag();
        for(byte index = 0; index < inventory.getContainerSize(); ++index) {
            ItemStack itemStack = inventory.getItem(index);
            if(!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundTag slot = new CompoundTag();
                slot.putInt("index", index);
                slots.add(itemStack.save(provider, slot));
            }
        }
        data.put(tagName, slots);
    }

    @Override
    public boolean addToSlot(Container inventory, int slot, ItemStack itemStack) {
        return addToSlot(inventory, slot, itemStack, false);
    }

    @Override
    public ItemStack addToStack(ItemStack itemStack, ItemStack toAdd) {
        if (ItemStack.isSameItemSameComponents(toAdd, itemStack)
                && itemStack.getCount() < itemStack.getMaxStackSize()) {
            toAdd = toAdd.copy();
            int toAddCount = Math.min(itemStack.getMaxStackSize() - itemStack.getCount(), toAdd.getCount());
            itemStack.grow(toAddCount);
            toAdd.shrink(toAddCount);
        }
        return toAdd;
    }

    @Override
    public ItemStack fillSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate) {
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

    @Override
    public boolean addToSlot(Container inventory, int slot, ItemStack itemStack, boolean simulate) {
        return fillSlot(inventory, slot, itemStack, simulate).isEmpty();
    }

    @Override
    public NonNullList<ItemStack> addToInventory(Container inventory, int[] slots, NonNullList<ItemStack> itemStacks, boolean simulate) {
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

    @Override
    public void addStackToList(NonNullList<ItemStack> itemStacks, ItemStack itemStack) {
        // Try to add the stack to one of the already-present stacks before adding a new element
        boolean added = false;
        ItemStack toAdd = itemStack;
        for (ItemStack existingOutputStack: itemStacks) {
            toAdd = this.addToStack(existingOutputStack, toAdd);
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
