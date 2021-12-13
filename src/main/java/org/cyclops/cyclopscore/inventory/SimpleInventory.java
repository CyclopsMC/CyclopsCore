package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A basic inventory implementation.
 * @author rubensworks
 *
 */
public class SimpleInventory implements INBTInventory, ISidedInventory {

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
    public int getContainerSize() {
        return contents.length;
    }

    @Override
    public ItemStack getItem(int slotId) {
        return contents[slotId];
    }

    @Override
    public ItemStack removeItem(int slotId, int count) {
        ItemStack stack = getItem(slotId);
        if (slotId < getContainerSize() && !stack.isEmpty()) {
            if (stack.getCount() > count) {
                ItemStack slotContents = stack.copy();
                ItemStack result = slotContents.split(count);
                setItem(slotId, slotContents);
                return result;
            }
            setItem(slotId, ItemStack.EMPTY);
            onInventoryChanged();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slotId, ItemStack itemstack) {
        if (slotId >= getContainerSize()) {
            return;
        }
        this.contents[slotId] = Objects.requireNonNull(itemstack);

        if (!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }
        onInventoryChanged();
    }

    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }

    protected void onInventoryChanged() {
        setChanged();
    }

    @Override
    public boolean stillValid(PlayerEntity entityplayer) {
        return true;
    }

    @Override
    public void startOpen(PlayerEntity playerIn) {

    }

    @Override
    public void stopOpen(PlayerEntity playerIn) {

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

        for (int j = 0; j < getContainerSize(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.size(); ++j) {
            CompoundNBT slot = nbttaglist.getCompound(j);
            int index;
            if (slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < getContainerSize()) {
                contents[index] = ItemStack.of(slot);
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
        for (byte index = 0; index < getContainerSize(); ++index) {
            ItemStack itemStack = getItem(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundNBT slot = new CompoundNBT();
                slots.add(slot);
                slot.putByte("Slot", index);
                itemStack.save(slot);
            }
        }
        data.put(tag, slots);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slotId) {
        ItemStack stackToTake = getItem(slotId);
        if (stackToTake.isEmpty()) {
            return ItemStack.EMPTY;
        }

        setItem(slotId, ItemStack.EMPTY);
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
    public boolean canPlaceItem(int i, ItemStack itemstack) {
        return i < getContainerSize() && i >= 0;
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < getContainerSize(); i++) {
            contents[i] = ItemStack.EMPTY;
        }
    }

    @Override
	public void setChanged() {
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
        for(int i = 0; i < getContainerSize(); i++) {
            if(!getItem(i).isEmpty()) {
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

    public IItemHandler getItemHandlerSided(Direction side) {
        return new SidedInvWrapper(this, side);
    }

    /**
     * @return The inventory state.
     */
    public int getState() {
        return hash;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }
}