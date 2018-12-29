package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.IValueNotifier;
import org.cyclops.cyclopscore.inventory.container.button.IButtonActionServer;
import org.cyclops.cyclopscore.inventory.container.button.IButtonClickAcceptorServer;
import org.cyclops.cyclopscore.inventory.slot.SlotArmor;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.packet.ValueNotifyPacket;

import java.util.Map;
import java.util.Set;

/**
 * A container with inventory.
 * @author rubensworks
 */
public abstract class InventoryContainer extends Container implements IButtonClickAcceptorServer<InventoryContainer>,
        IValueNotifier, IValueNotifiable {

    private static final EntityEquipmentSlot[] EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {
            EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    protected static final int ITEMBOX = 18;

    private final Map<Integer, IButtonActionServer<InventoryContainer>> buttonActions = Maps.newHashMap();
    private final Map<Integer, NBTTagCompound> values = Maps.newHashMap();
    private int nextValueId = 0;
    private IValueNotifiable guiValueListener = null;

    private IInventory playerIInventory;
    protected final EntityPlayer player;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /* The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?) */
    private int dragMode = -1;
    /** The current drag event (0 : start, 1 : add slot : 2 : end) */
    private int dragEvent;
    /** The list of slots where the itemstack holds will be distributed */
    private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();

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
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        if(!player.getEntityWorld().isRemote) {
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
        slot.xPos += offsetX;
        slot.yPos += offsetY;
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
        int rows = 3;
        int cols = 9;

        // Player hotbar
        addInventory(inventory, 0, offsetX, offsetY + 58, 1, cols);

        // Player inventory
        addInventory(inventory, cols, offsetX, offsetY, rows, cols);
    }

    /**
     * Add player armor inventory to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerArmorInventory(InventoryPlayer inventory, int offsetX, int offsetY) {
        for (int k = 0; k < 4; ++k) {
            EntityEquipmentSlot equipmentSlot = EQUIPMENT_SLOTS[k];
            addSlotToContainer(new SlotArmor(inventory, 4 * 9 + (3 - k), offsetX, offsetY + k * ITEMBOX, inventory.player, equipmentSlot));
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
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotID);
        int slots = getSizeInventory();
        
        if(slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack().copy();
            stack = stackInSlot.copy();

            if(slotID < slots) { // Click in tile -> player inventory
                if(!mergeItemStack(stackInSlot, getSlotStart(slotID, slots, true), getSlotRange(slotID, inventorySlots.size(), true), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!mergeItemStack(stackInSlot, getSlotStart(slotID, 0, false), getSlotRange(slotID, slots, false), false)) { // Click in player inventory -> tile
                return ItemStack.EMPTY;
            }
            
            if(stackInSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.putStack(stackInSlot);
            }

            if(stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
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
            while (stack.getCount() > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)) {
                slot = this.inventorySlots.get(slotIndex);
                int maxSlotSize = Math.min(slot.getSlotStackLimit(), maxStack);
                existingStack = slot.getStack().copy();

                if (slot.isItemValid(stack) && !existingStack.isEmpty() && existingStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
                    int existingSize = existingStack.getCount() + stack.getCount();
                    if (existingSize <= maxSlotSize) {
                        stack.setCount(0);
                        existingStack.setCount(existingSize);
                        slot.putStack(existingStack);
                        successful = true;
                    } else if (existingStack.getCount() < maxSlotSize) {
                        stack.shrink(maxSlotSize - existingStack.getCount());
                        existingStack.setCount(maxSlotSize);
                        slot.putStack(existingStack);
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

        if (stack.getCount() > 0) {
            if (reverse) {
                slotIndex = slotRange - 1;
            } else {
                slotIndex = slotStart;
            }

            while (stack.getCount() > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)) {
                slot = this.inventorySlots.get(slotIndex);
                existingStack = slot.getStack().copy();

                if (slot.isItemValid(stack) && existingStack.isEmpty()) {
                    int placedAmount = Math.min(stack.getCount(), slot.getSlotStackLimit());
                    ItemStack toPut = stack.copy();
                    toPut.setCount(placedAmount);
                    slot.putStack(toPut);
                    stack.shrink(placedAmount);
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
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
        Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
        InventoryPlayer inventoryplayer = player.inventory;
        if (clickType == ClickType.QUICK_CRAFT) {
            // Copied and adjusted from net.minecraft.inventory.Container
            int j1 = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent) {
                this.resetDrag();
            } else if (inventoryplayer.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slotId);
                ItemStack itemstack12 = inventoryplayer.getItemStack();

                if (slot7 != null && canAddItemToSlot(slot7, itemstack12, true) && slot7.isItemValid(itemstack12) && (this.dragMode == 2 || itemstack12.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    ItemStack itemstack9 = inventoryplayer.getItemStack().copy();
                    int k1 = inventoryplayer.getItemStack().getCount();
                    int phantomCount = 0; // Added

                    for (Slot slot8 : this.dragSlots) {
                        ItemStack itemstack13 = inventoryplayer.getItemStack();

                        if (slot8 != null && canAddItemToSlot(slot8, itemstack13, true) && slot8.isItemValid(itemstack13) && (this.dragMode == 2 || itemstack13.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                            int k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));

                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);

                            // --- Added ---
                            if (slot8 instanceof SlotExtended && ((SlotExtended) slot8).isPhantom()) {
                                phantomCount += itemstack14.getCount() - j3;
                            }
                        }
                    }

                    itemstack9.setCount(k1 + phantomCount); // Changed
                    inventoryplayer.setItemStack(itemstack9);
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
            return ItemStack.EMPTY;
        } else if (this.dragEvent != 0) {
            this.resetDrag();
            return ItemStack.EMPTY;
        } else if (slot instanceof SlotExtended && ((SlotExtended) slot).isPhantom()) { // Phantom slot code based on Buildcraft
            return slotClickPhantom(slot, dragType, clickType, player);
        } else {
            return super.slotClick(slotId, dragType, clickType, player);
        }
    }

    @Override
    protected void resetDrag() {
        super.resetDrag();
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, ClickType clickType, EntityPlayer player) {
        ItemStack stack = ItemStack.EMPTY;

        if (mouseButton == 2) {
            if (((SlotExtended) slot).isAdjustable()) {
                slot.putStack(ItemStack.EMPTY);
            }
        } else if (mouseButton == 0 || mouseButton == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();

            if (!stackSlot.isEmpty()) {
                stack = stackSlot.copy();
            }

            if (stackSlot.isEmpty()) {
                if (!stackHeld.isEmpty() && slot.isItemValid(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, mouseButton, clickType);
                }
            } else if (stackHeld.isEmpty()) {
                adjustPhantomSlot(slot, mouseButton, clickType);
                slot.onTake(player, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (ItemMatch.areItemStacksEqual(stackSlot, stackHeld, ItemMatch.ITEM | ItemMatch.DAMAGE | ItemMatch.NBT)) {
                    adjustPhantomSlot(slot, mouseButton, clickType);
                } else {
                    fillPhantomSlot(slot, stackHeld, mouseButton, clickType);
                }
            }
        }
        return stack;
    }

    protected void adjustPhantomSlot(Slot slot, int mouseButton, ClickType clickType) {
        if (!((SlotExtended) slot).isAdjustable()) {
            return;
        }
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (clickType == ClickType.QUICK_MOVE) {
            stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
        }

        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }

        stackSlot.setCount(stackSize);

        if (stackSlot.getCount() <= 0) {
            slot.putStack(ItemStack.EMPTY);
        }
    }

    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, ClickType clickType) {
        if (!((SlotExtended) slot).isAdjustable()) {
            return;
        }
        int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
        if (stackSize > slot.getSlotStackLimit()) {
            stackSize = slot.getSlotStackLimit();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.setCount(stackSize);

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
        IButtonActionServer<InventoryContainer> action;
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
            if (!player.getEntityWorld().isRemote) { // server -> client
                CyclopsCore._instance.getPacketHandler().sendToPlayer(new ValueNotifyPacket(getGuiModId(), getGuiId(), valueId, value), (EntityPlayerMP) player);
            } else { // client -> server
                CyclopsCore._instance.getPacketHandler().sendToServer(new ValueNotifyPacket(getGuiModId(), getGuiId(), valueId, value));
            }
            values.put(valueId, value);
        }
    }

    @Override
    public NBTTagCompound getValue(int valueId) {
        return values.get(valueId);
    }

    @Override
    public Set<Integer> getValueIds() {
        return values.keySet();
    }

    @Override
    public void onUpdate(int valueId, NBTTagCompound value) {
        values.put(valueId, value);
        if(guiValueListener != null) {
            guiValueListener.onUpdate(valueId, value);
        }
    }

}
