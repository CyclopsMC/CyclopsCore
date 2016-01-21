package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.IValueNotifier;
import org.cyclops.cyclopscore.inventory.container.button.IButtonActionServer;
import org.cyclops.cyclopscore.inventory.container.button.IButtonClickAcceptorServer;
import org.cyclops.cyclopscore.inventory.slot.SlotArmor;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.packet.ValueNotifyPacket;

import java.util.Map;

/**
 * A container with inventory.
 * @author rubensworks
 */
public abstract class InventoryContainer extends Container implements IButtonClickAcceptorServer<InventoryContainer>,
        IValueNotifier, IValueNotifiable {
    
    protected static final int ITEMBOX = 18;

    private final Map<Integer, IButtonActionServer> buttonActions = Maps.newHashMap();
    private final Map<Integer, NBTTagCompound> values = Maps.newHashMap();
    private int nextValueId = 0;
    private IValueNotifiable guiValueListener = null;

    private IInventory playerIInventory;
    protected final EntityPlayer player;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /**
     * Make a new TileInventoryContainer.
     * @param inventory The player inventory.
     */
    public InventoryContainer(InventoryPlayer inventory) {
        this.playerIInventory = inventory;
        this.player = inventory.player;
    }

    /**
     * Set the listener that will be triggered when a value in this container is updated by the server.
     * @param listener The listener that will be triggered.
     */
    public void setGuiValueListener(IValueNotifiable listener) {
        this.guiValueListener = listener;
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        if(!MinecraftHelpers.isClientSide()) {
            initializeValues();
        }
    }

    /**
     * This is the place to initialize values server-side so that they can be sent to the client for the first time.
     * This is only called on the server.
     * Make sure not to initialize the value id's here, but do that inside the constructor, because these must be equal
     * for client and server.
     */
    protected void initializeValues() {

    }
    
    protected Slot createNewSlot(IInventory inventory, int index, int x, int y) {
    	return new Slot(inventory, index, x, y);
    }

    protected Slot addSlotToContainer(Slot slot) {
        slot.xDisplayPosition += offsetX;
        slot.yDisplayPosition += offsetY;
        return super.addSlotToContainer(slot);
    }
    
    protected void addInventory(IInventory inventory, int indexOffset, int offsetX, int offsetY, int rows, int cols) {
    	for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Slot params: id, x-coord, y-coord (coords are relative to gui box)
                addSlotToContainer(createNewSlot(inventory, x + y * cols + indexOffset, offsetX + x * ITEMBOX, offsetY + y * ITEMBOX));
            }
        }
    }
    
    /**
     * Add player inventory and hotbar to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerInventory(InventoryPlayer inventory, int offsetX, int offsetY) {
        // Player inventory
        int rows = 3;
        int cols = 9;
        addInventory(inventory, cols, offsetX, offsetY, rows, cols);
        
        // Player hotbar
        offsetY += 58;
        addInventory(inventory, 0, offsetX, offsetY, 1, cols);
    }

    /**
     * Add player armor inventory to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerArmorInventory(InventoryPlayer inventory, int offsetX, int offsetY) {
        for (int y = 0; y < 4; y++) {
            addSlotToContainer(new SlotArmor(inventory, 4 * 9 + (3 - y), offsetX, offsetY + y * ITEMBOX, inventory.player, y));
        }
    }
    
    protected abstract int getSizeInventory();
    
    protected int getSlotStart(int originSlot, int slotStart, boolean reverse) {
    	return slotStart;
    }
    
    protected int getSlotRange(int originSlot, int slotRange, boolean reverse) {
    	return slotRange;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
        ItemStack stack = null;
        Slot slot = (Slot) inventorySlots.get(slotID);
        int slots = getSizeInventory();
        
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();

            if(slotID < slots) { // Click in tile -> player inventory
                if(!mergeItemStack(stackInSlot, getSlotStart(slotID, slots, true), getSlotRange(slotID, inventorySlots.size(), true), true)) {
                    return null;
                }
            } else if(!mergeItemStack(stackInSlot, getSlotStart(slotID, 0, false), getSlotRange(slotID, slots, false), false)) { // Click in player inventory -> tile
                return null;
            }
            
            if(stackInSlot.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if(stackInSlot.stackSize == stack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, stackInSlot);
        }
        
        return stack;
    }
    
    @Override
    protected boolean mergeItemStack(ItemStack stack, int slotStart, int slotRange, boolean reverse) {
        boolean successful = false;
        int slotIndex = slotStart;
        int maxStack = stack.getMaxStackSize();

        if (reverse) {
            slotIndex = slotRange - 1;
        }

        Slot slot;
        ItemStack existingStack;

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)) {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                int maxSlotSize = Math.min(slot.getSlotStackLimit(), maxStack);
                existingStack = slot.getStack();

                if (slot.isItemValid(stack) && existingStack != null && existingStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
                    int existingSize = existingStack.stackSize + stack.stackSize;
                    if (existingSize <= maxSlotSize) {
                        stack.stackSize = 0;
                        existingStack.stackSize = existingSize;
                        slot.onSlotChanged();
                        successful = true;
                    } else if (existingStack.stackSize < maxSlotSize) {
                        stack.stackSize -= maxSlotSize - existingStack.stackSize;
                        existingStack.stackSize = maxSlotSize;
                        slot.onSlotChanged();
                        successful = true;
                    }
                }

                if (reverse) {
                    --slotIndex;
                } else {
                    ++slotIndex;
                }
            }
        }

        if (stack.stackSize > 0) {
            if (reverse) {
                slotIndex = slotRange - 1;
            } else {
                slotIndex = slotStart;
            }

            while (stack.stackSize > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)) {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                existingStack = slot.getStack();

                if (slot.isItemValid(stack) && existingStack == null) {
                    int placedAmount = Math.min(stack.stackSize, slot.getSlotStackLimit());
                    ItemStack toPut = stack.copy();
                    toPut.stackSize = placedAmount;
                    slot.putStack(toPut);
                    slot.onSlotChanged();
                    stack.stackSize -= placedAmount;
                    successful = true;
                }

                if (reverse) {
                    --slotIndex;
                } else {
                    ++slotIndex;
                }
            }
        }

        return successful;
    }

    /**
     * Get the inventory of the player for which this container is instantiated.
     * @return The player inventory.
     */
    public IInventory getPlayerIInventory() {
        return playerIInventory;
    }

    @Override
    public ItemStack slotClick(int slotId, int arg, int function, EntityPlayer player) {
        Slot slot = slotId < 0 ? null : (Slot) this.inventorySlots.get(slotId);
        // Phantom slot code based on Buildcraft
        if(slot instanceof SlotExtended && ((SlotExtended) slot).isPhantom()) {
            return slotClickPhantom(slot, arg, function, player);
        }
        return super.slotClick(slotId, arg, function, player);
    }

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
        ItemStack stack = null;

        if (mouseButton == 2) {
            if (((SlotExtended) slot).isAdjustable()) {
                slot.putStack(null);
            }
        } else if (mouseButton == 0 || mouseButton == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();

            if (stackSlot != null) {
                stack = stackSlot.copy();
            }

            if (stackSlot == null) {
                if (stackHeld != null && slot.isItemValid(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
                }
            } else if (stackHeld == null) {
                adjustPhantomSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (ItemStack.areItemStacksEqual(stackSlot, stackHeld)) {
                    adjustPhantomSlot(slot, mouseButton, modifier);
                } else {
                    fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
                }
            }
        }
        return stack;
    }

    protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
        if (!((SlotExtended) slot).isAdjustable()) {
            return;
        }
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1) {
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        }

        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }

        stackSlot.stackSize = stackSize;

        if (stackSlot.stackSize <= 0) {
            slot.putStack(null);
        }
    }

    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
        if (!((SlotExtended) slot).isAdjustable()) {
            return;
        }
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;

        slot.putStack(phantomStack);
    }

    @Override
    public void putButtonAction(int buttonId, IButtonActionServer<InventoryContainer> action) {
        buttonActions.put(buttonId, action);
    }

    @Override
    public boolean requiresAction(int buttonId) {
        return buttonActions.containsKey(buttonId);
    }

    @Override
    public void onButtonClick(int buttonId) {
        IButtonActionServer action;
        if((action = buttonActions.get(buttonId)) != null) {
            action.onAction(buttonId, this);
        }
    }

    /**
     * @return The next unique value id.
     */
    protected int getNextValueId() {
        return nextValueId++;
    }

    @Override
    public void setValue(int valueId, NBTTagCompound value) {
        if (!values.containsKey(valueId) || !values.get(valueId).equals(value)) {
            if (!MinecraftHelpers.isClientSide()) { // server -> client
                CyclopsCore._instance.getPacketHandler().sendToPlayer(new ValueNotifyPacket(valueId, value), (EntityPlayerMP) player);
            } else { // client -> server
                CyclopsCore._instance.getPacketHandler().sendToServer(new ValueNotifyPacket(valueId, value));
            }
            values.put(valueId, value);
        }
    }

    @Override
    public NBTTagCompound getValue(int valueId) {
        return values.get(valueId);
    }

    @Override
    public void onUpdate(int valueId, NBTTagCompound value) {
        values.put(valueId, value);
        if(guiValueListener != null) {
            guiValueListener.onUpdate(valueId, value);
        }
    }

}
