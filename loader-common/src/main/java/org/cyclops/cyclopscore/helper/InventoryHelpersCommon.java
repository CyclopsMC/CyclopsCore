package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
                player.getInventory().setItem(hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : 40, newStackPart);
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
    public void readFromNBT(Container inventory, ValueInput data, String tagName) {
        ValueInput.ValueInputList nbttaglist = data.childrenList(tagName).orElseThrow();

        for(int j = 0; j < inventory.getContainerSize(); j++) {
            inventory.setItem(j, ItemStack.EMPTY);
        }

        for (ValueInput slot : nbttaglist) {
            int index = slot.getByteOr("Slot", (byte) 0);
            if(index >= 0 && index < inventory.getContainerSize()) {
                inventory.setItem(index, slot.read("Item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public void writeToNBT(Container inventory, ValueOutput data, String tagName) {
        ValueOutput.ValueOutputList slots = data.childrenList(tagName);
        for(byte index = 0; index < inventory.getContainerSize(); ++index) {
            ItemStack itemStack = inventory.getItem(index);
            if(!itemStack.isEmpty() && itemStack.getCount() > 0) {
                ValueOutput slot = slots.addChild();
                slot.putInt("index", index);
                slot.store("Item", ItemStack.OPTIONAL_CODEC, itemStack);
            }
        }
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
