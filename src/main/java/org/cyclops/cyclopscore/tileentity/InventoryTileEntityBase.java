package org.cyclops.cyclopscore.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.cyclops.cyclopscore.Capabilities;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.inventory.INBTInventory;
import org.cyclops.cyclopscore.inventory.TileInventoryState;

import java.util.Random;

/**
 * A TileEntity with an internal inventory.
 * @author rubensworks
 *
 */
public abstract class InventoryTileEntityBase extends CyclopsTileEntity implements ISidedInventory {

    private static final Random RAND = new Random();

    protected boolean sendUpdateOnInventoryChanged = false;
    protected final EnumFacingMap<IItemHandler> sidedInventoryHandlers;
    private int inventoryHash = 0;

    public InventoryTileEntityBase() {
        this.sidedInventoryHandlers = EnumFacingMap.newMap();
        addCapabilityInternal(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new InvWrapper(this));
        for(EnumFacing side : EnumFacing.VALUES) {
            addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side, new SidedInvWrapper(this, side));
        }
        if (Capabilities.INVENTORY_STATE != null) {
            addInventoryStateCapability();
        }
    }

    protected void addInventoryStateCapability() {
        addCapabilityInternal(Capabilities.INVENTORY_STATE, new TileInventoryState(this));
    }
    
    /**
     * Get the internal inventory.
     * @return The inventory.
     */
    public abstract INBTInventory getInventory();
    
    @Override
    public int getSizeInventory() {
        return getInventory().getSizeInventory();
    }
    
    @Override
    public ItemStack getStackInSlot(int slotId) {
        if(slotId >= getSizeInventory() || slotId < 0)
            return ItemStack.EMPTY;
        return getInventory().getStackInSlot(slotId);
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        ItemStack itemStack  = getInventory().decrStackSize(slotId, count);
        onInventoryChanged();
        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotId) {
        return getInventory().removeStackFromSlot(slotId);
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        getInventory().setInventorySlotContents(slotId, itemstack);
        onInventoryChanged();
    }

    protected void updateInventoryHash() {
        inventoryHash++;
    }

    protected void onInventoryChanged() {
        if(isSendUpdateOnInventoryChanged())
            sendUpdate();
        updateInventoryHash();
    }

    @Override
    public String getName() {
        return getInventory().getName();
    }

    @Override
    public boolean hasCustomName() {
        return getInventory().hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return getInventory().getDisplayName();
    }

    @Override
    public int getInventoryStackLimit() {
        return getInventory().getInventoryStackLimit();
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return world.getTileEntity(getPos()) == this && entityPlayer.getDistanceSq(getPos().add(0.5D, 0.5D, 0.5D)) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
        getInventory().openInventory(playerIn);
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
        getInventory().closeInventory(playerIn);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getInventory().isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return getInventory().getField(id);
    }

    @Override
    public void setField(int id, int value) {
        getInventory().setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return getInventory().getFieldCount();
    }

    @Override
    public void clear() {
        getInventory().clear();
    }

    @Override
    public void markDirty() {
        updateInventoryHash();
        super.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        INBTInventory inventory = getInventory();
        if(inventory != null) {
            inventory.readFromNBT(tag);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        INBTInventory inventory = getInventory();
        if(inventory != null) {
            inventory.writeToNBT(tag);
        }
        return tag;
    }
    
    protected boolean canAccess(int slot, EnumFacing side) {
        boolean canAccess = false;
        for(int slotAccess : getSlotsForFace(side)) {
            if(slotAccess == slot)
                canAccess = true;
        }
        return canAccess;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return canAccess(slot, side) && this.isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, EnumFacing side) {
        return canAccess(slot, side);
    }

    @Override
    public boolean isEmpty() {
        return getInventory().isEmpty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return getInventory().isUsableByPlayer(player);
    }

    /**
     * If this tile should send blockState updates when the inventory has changed.
     * @return If it should send blockState updates.
     */
    public boolean isSendUpdateOnInventoryChanged() {
        return sendUpdateOnInventoryChanged;
    }

    /**
     * If this tile should send blockState updates when the inventory has changed.
     * @param sendUpdateOnInventoryChanged If it should send blockState updates.
     */
    public void setSendUpdateOnInventoryChanged(
            boolean sendUpdateOnInventoryChanged) {
        this.sendUpdateOnInventoryChanged = sendUpdateOnInventoryChanged;
    }

    public int getInventoryHash() {
        return this.inventoryHash;
    }
}
