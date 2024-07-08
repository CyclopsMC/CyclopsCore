package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A basic inventory implementation.
 * @author rubensworks
 *
 */
public class SimpleInventory implements INBTInventory, WorldlyContainer {

    public static final Codec<SimpleInventory> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(Codec.INT.fieldOf("size").forGetter(SimpleInventory::getContainerSize),
                    Codec.INT.fieldOf("stackLimit").forGetter(SimpleInventory::getMaxStackSize),
                    ItemStack.CODEC.listOf().fieldOf("contents").forGetter(i -> Arrays.asList(i.getItemStacks())))
            .apply(builder, SimpleInventory::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleInventory> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SimpleInventory::getContainerSize,
            ByteBufCodecs.INT, SimpleInventory::getMaxStackSize,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), i -> Arrays.asList(i.getItemStacks()),
            SimpleInventory::new
    );

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

    public SimpleInventory(int size, int stackLimit, List<ItemStack> contents) {
        this.contents = new ItemStack[size];
        for (int i = 0; i < this.contents.length; i++) {
            this.contents[i] = contents.get(i);
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
    public boolean stillValid(Player entityplayer) {
        return true;
    }

    @Override
    public void startOpen(Player playerIn) {

    }

    @Override
    public void stopOpen(Player playerIn) {

    }

    @Override
    public void read(HolderLookup.Provider provider, CompoundTag data) {
        readFromNBT(provider, data, "items");
    }

    public void readFromNBT(HolderLookup.Provider provider, CompoundTag data, String tag) {
        ListTag nbttaglist = data.getList(tag, Tag.TAG_COMPOUND);

        for (int j = 0; j < getContainerSize(); ++j)
            contents[j] = ItemStack.EMPTY;

        for (int j = 0; j < nbttaglist.size(); ++j) {
            CompoundTag slot = nbttaglist.getCompound(j);
            int index;
            if (slot.contains("index")) {
                index = slot.getInt("index");
            } else {
                index = slot.getByte("Slot");
            }
            if (index >= 0 && index < getContainerSize()) {
                contents[index] = ItemStack.parseOptional(provider, slot);
            }
        }
    }

    @Override
    public void write(HolderLookup.Provider provider, CompoundTag data) {
        writeToNBT(provider, data, "items");
    }

    public void writeToNBT(HolderLookup.Provider provider, CompoundTag data, String tag) {
        ListTag slots = new ListTag();
        for (byte index = 0; index < getContainerSize(); ++index) {
            ItemStack itemStack = getItem(index);
            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                CompoundTag slot = new CompoundTag();
                slot.putByte("Slot", index);
                slots.add(itemStack.save(provider, slot));
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
     * Get the array of {@link net.minecraft.world.item.ItemStack} inside this inventory.
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
    public CompoundTag toNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        write(provider, tag);
        return tag;
    }

    @Override
    public void fromNBT(HolderLookup.Provider provider, CompoundTag tag) {
        read(provider, tag);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleInventory)) return false;

        SimpleInventory that = (SimpleInventory) o;

        if (stackLimit != that.stackLimit) return false;
        if (contents.length != that.contents.length) return false;
        for (int i = 0; i < contents.length; i++) {
            if (!ItemStack.isSameItemSameComponents(contents[i], that.contents[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
