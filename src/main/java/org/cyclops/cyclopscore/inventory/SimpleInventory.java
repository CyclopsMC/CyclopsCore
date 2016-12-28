package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import java.util.List;
import java.util.Objects;

/**
 * A basic inventory implementation.
 * @author rubensworks
 *
 */
public class SimpleInventory implements INBTInventory {

    protected final ItemStack[] _contents;
    private final String _name;
    private final int _stackLimit;
    private final List<IDirtyMarkListener> dirtyMarkListeners = Lists.newLinkedList();

    /**
     * Default constructor for NBT persistence, don't call this yourself.
     */
    public SimpleInventory() {
        this(0, "", 0);
    }

    /**
     * Make a new instance.
     * @param size The amount of slots in the inventory.
     * @param name The name of the inventory, used for NBT storage.
     * @param stackLimit The stack limit for each slot.
     */
    public SimpleInventory(int size, String name, int stackLimit) {
        _contents = new ItemStack[size];
        for (int i = 0; i < _contents.length; i++) {
            _contents[i] = ItemStack.EMPTY;
        }
        _name = name;
        _stackLimit = stackLimit;
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
        return _contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return _contents[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < _contents.length && _contents[slotId] != null) {
            if (_contents[slotId].getCount() > count) {
                ItemStack result = _contents[slotId].splitStack(count);
                onInventoryChanged();
                return result;
            }
            ItemStack stack = _contents[slotId];
            _contents[slotId] = ItemStack.EMPTY;
            onInventoryChanged();
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= _contents.length) {
            return;
        }
        this._contents[slotId] = Objects.requireNonNull(itemstack);

        if (!itemstack.isEmpty() && itemstack.getCount() > this.getInventoryStackLimit()) {
            itemstack.setCount(this.getInventoryStackLimit());
        }
        onInventoryChanged();
    }

    @Override
    public int getInventoryStackLimit() {
        return _stackLimit;
    }

    protected void onInventoryChanged() {
        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {

    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {

    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        readFromNBT(data, "items");
    }

    /**
     * Read inventory data from the given NBT.
     * @param data The NBT data containing inventory data.
     * @param tag The NBT tag name where the info is located.
     */
    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data.getTagList(tag, MinecraftHelpers.NBTTag_Types.NBTTagCompound.ordinal());
        
        for (int j = 0; j < _contents.length; ++j)
            _contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
            NBTTagCompound slot = nbttaglist.getCompoundTagAt(j);
            int index;
            if (slot.hasKey("index")) {
                index = slot.getInteger("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < _contents.length) {
                _contents[index] = new ItemStack(slot);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound data) {
        writeToNBT(data, "items");
    }

    /**
     * Write inventory data to the given NBT.
     * @param data The NBT tag that will receive inventory data.
     * @param tag The NBT tag name where the info must be located.
     */
    public void writeToNBT(NBTTagCompound data, String tag) {
        NBTTagList slots = new NBTTagList();
        for (byte index = 0; index < _contents.length; ++index) {
            if (_contents[index] != null && _contents[index].getCount() > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setByte("Slot", index);
                _contents[index].writeToNBT(slot);
            }
        }
        data.setTag(tag, slots);
    }

    @Override
    public ItemStack removeStackFromSlot(int slotId) {
        ItemStack stackToTake = this._contents[slotId];
        this._contents[slotId] = ItemStack.EMPTY;
        return stackToTake;
    }

    /**
     * Get the array of {@link net.minecraft.item.ItemStack} inside this inventory.
     * @return The items in this inventory.
     */
    public ItemStack[] getItemStacks() {
        return _contents;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i < _contents.length && i >= 0;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < getSizeInventory(); i++) {
            _contents[i] = ItemStack.EMPTY;
        }
    }

    @Override
	public void markDirty() {
        List<IDirtyMarkListener> dirtyMarkListeners;
        synchronized (this) {
            dirtyMarkListeners = Lists.newLinkedList(this.dirtyMarkListeners);
        }
		for(IDirtyMarkListener dirtyMarkListener : dirtyMarkListeners) {
            dirtyMarkListener.onDirty();
        }
	}

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(this.getName());
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
    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

    @Override
    public void fromNBT(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}