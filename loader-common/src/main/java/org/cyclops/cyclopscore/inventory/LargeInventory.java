package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * A large inventory implementation.
 * @author rubensworks
 *
 */
public class LargeInventory extends SimpleInventory {

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public LargeInventory() {
        this(0, 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     */
    public LargeInventory(int size, int stackLimit) {
        super(size, stackLimit);
    }

    public void readFromNBT(ValueInput data, String tag) {
        ValueInput.ValueInputList nbttaglist = data.childrenList(tag).orElseThrow();

        for (int j = 0; j < getContainerSize(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (ValueInput slot : nbttaglist) {
            int index = slot.getIntOr("Slot", (byte) 0);
            if (index >= 0 && index < getContainerSize()) {
                contents[index] = slot.read("Item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
            }
        }
    }

    public void writeToNBT(ValueOutput data, String tag) {
        ValueOutput.ValueOutputList slots = data.childrenList(tag);
        for (int index = 0; index < getContainerSize(); ++index) {
            ItemStack itemStack = getItem(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                ValueOutput slot = slots.addChild();
                slot.putInt("Slot", index);
                slot.store("Item", ItemStack.OPTIONAL_CODEC, itemStack);
            }
        }
    }

}
