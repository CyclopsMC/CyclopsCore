package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.cyclops.commoncapabilities.api.capability.inventorystate.IInventoryState;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import java.util.List;
import java.util.Objects;

/**
 * A basic inventory implementation.
 * @author rubensworks
 *
 */
public class SimpleInventory implements INBTInventory, IInventoryState {

    protected final ItemStack[] contents;
    private final int stackLimit;
    private final List<IDirtyMarkListener> dirtyMarkListeners = Lists.newLinkedList();

    private int hash;

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public SimpleInventory() {
        this(0, 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     */
    public SimpleInventory(int size, int stackLimit) {
        contents = new ItemStack[size];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = ItemStack.EMPTY;
        }
        this.stackLimit = stackLimit;
    }

    /**
     * Add a dirty marking listener.
     * @param dirtyMarkListener The dirty mark listener.
     */
    public synchronized void addDirtyMarkListener(IDirtyMarkListener dirtyMarkListener) {
        this.dirtyMarkListeners.add(dirtyMarkListener);
    }

    /**
     * Remove a dirty marking listener.
     * @param dirtyMarkListener The dirty mark listener.
     */
    public synchronized void removeDirtyMarkListener(IDirtyMarkListener dirtyMarkListener) {
        this.dirtyMarkListeners.remove(dirtyMarkListener);
    }

    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return contents[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        ItemStack stack = getStackInSlot(slotId);
        if (slotId < getSizeInventory() && !stack.isEmpty()) {
            if (stack.getCount() > count) {
                ItemStack slotContents = stack.copy();
                ItemStack result = slotContents.split(count);
                setInventorySlotContents(slotId, slotContents);
                return result;
            }
            setInventorySlotContents(slotId, ItemStack.EMPTY);
            onInventoryChanged();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= getSizeInventory()) {
            return;
        }
        this.contents[slotId] = Objects.requireNonNull(itemstack);

        if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }
        onInventoryChanged();
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    protected void onInventoryChanged() {
        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity entityplayer) {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity playerIn) {

    }

    @Override
    public void closeInventory(PlayerEntity playerIn) {

    }

    @Override
    public void read(CompoundNBT data) {
        readFromNBT(data, "items");
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     * @param tag The NBT tag name where the info is located.
     */
    public void readFromNBT(CompoundNBT data, String tag) {
        ListNBT nbttaglist = data.getList(tag, Constants.NBT.TAG_COMPOUND);

        for (int j = 0; j < getSizeInventory(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.size(); ++j) {
            CompoundNBT slot = nbttaglist.getCompound(j);
            int index;
            if (slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < getSizeInventory()) {
                contents[index] = ItemStack.read(slot);
            }
        }
    }

    @Override
    public void write(CompoundNBT data) {
        writeToNBT(data, "items");
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(CompoundNBT data, String tag) {
        ListNBT slots = new ListNBT();
        for (byte index = 0; index < getSizeInventory(); ++index) {
            ItemStack itemStack = getStackInSlot(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundNBT slot = new CompoundNBT();
                slots.add(slot);
                slot.putByte("Slot", index);
                itemStack.write(slot);
            }
        }
        data.put(tag, slots);
    }

    @Override
    public ItemStack removeStackFromSlot(int slotId) {
        ItemStack stackToTake = getStackInSlot(slotId);
        if (stackToTake.isEmpty()) {
            return ItemStack.EMPTY;
        }

        setInventorySlotContents(slotId, ItemStack.EMPTY);
        return stackToTake;
    }

    /**
     * Get the array of {@link net.minecraft.item.ItemStack} inside this inventory.
     * @return The items in this inventory.
     */
    public ItemStack[] getItemStacks() {
        return contents;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i < getSizeInventory() && i >= 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < getSizeInventory(); i++) {
            contents[i] = ItemStack.EMPTY;
        }
    }

    @Override
	public void markDirty() {
        this.hash++;
        List<IDirtyMarkListener> dirtyMarkListeners;
        synchronized (this) {
            dirtyMarkListeners = Lists.newLinkedList(this.dirtyMarkListeners);
        }
		for(IDirtyMarkListener dirtyMarkListener : dirtyMarkListeners) {
            dirtyMarkListener.onDirty();
        }
	}

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory(); i++) {
            if(!getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        write(tag);
        return tag;
    }

    @Override
    public void fromNBT(CompoundNBT tag) {
        read(tag);
    }

    public IItemHandler getItemHandler() {
        return new InvWrapper(this);
    }


    @Override
    public int getState() {
        return hash;
    }
}