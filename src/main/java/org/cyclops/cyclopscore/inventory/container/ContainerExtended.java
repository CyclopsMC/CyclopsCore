package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.inventory.IValueNotifier;
import org.cyclops.cyclopscore.inventory.container.button.IContainerButtonAction;
import org.cyclops.cyclopscore.inventory.container.button.IContainerButtonClickAcceptorServer;
import org.cyclops.cyclopscore.inventory.slot.SlotArmor;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.packet.ValueNotifyPacket;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A container with inventory.
 * @author rubensworks
 */
public abstract class ContainerExtended extends Container implements IContainerButtonClickAcceptorServer<ContainerExtended>,
        IValueNotifier, IValueNotifiable {

    private static final EquipmentSlotType[] EQUIPMENT_SLOTS = new EquipmentSlotType[] {
            EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    protected static final int ITEMBOX = 18;

    private final Map<String, IContainerButtonAction<ContainerExtended>> buttonActions = Maps.newHashMap();
    private final Map<Integer, CompoundNBT> values = Maps.newHashMap();
    private final List<SyncedGuiVariable<?>> syncedGuiVariables = Lists.newArrayList();
    private int nextValueId = 0;
    private IValueNotifiable guiValueListener = null;

    private IInventory playerIInventory;
    protected final PlayerEntity player;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /* The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?) */
    private int dragMode = -1;
    /** The current drag event (0 : start, 1 : add slot : 2 : end) */
    private int dragEvent;
    /** The list of slots where the itemstack holds will be distributed */
    private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();

    /**
     * Make a new ContainerExtended.
     * @param type The container type.
     * @param id The container id.
     * @param inventory The player inventory.
     */
    public ContainerExtended(@Nullable ContainerType<?> type, int id, PlayerInventory inventory) {
        super(type, id);
        this.playerIInventory = inventory;
        this.player = inventory.player;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (!player.world.isRemote()) {
            for (SyncedGuiVariable<?> syncedGuiVariable : this.syncedGuiVariables) {
                syncedGuiVariable.detectAndSendChanges();
            }
        }
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
        if(!player.getEntityWorld().isRemote()) {
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

    static void setSlotPos(Slot slot, String fieldName, int newValue) {
        try {
            Field field = ObfuscationReflectionHelper.findField(Slot.class, fieldName);

            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(slot, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setSlotPosX(Slot slot, int newValue) {
        setSlotPos(slot, "field_75223_e", newValue);
    }

    public static void setSlotPosY(Slot slot, int newValue) {
        setSlotPos(slot, "field_75221_f", newValue);
    }

    @Override
    protected Slot addSlot(Slot slot) {
        setSlotPosX(slot, slot.xPos + offsetX);
        setSlotPosY(slot, slot.yPos + offsetY);
        return super.addSlot(slot);
    }
    
    protected void addInventory(IInventory inventory, int indexOffset, int offsetX, int offsetY, int rows, int cols) {
    	for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Slot params: id, x-coord, y-coord (coords are relative to gui box)
                addSlot(createNewSlot(inventory, x + y * cols + indexOffset, offsetX + x * ITEMBOX, offsetY + y * ITEMBOX));
            }
        }
    }
    
    /**
     * Add player inventory and hotbar to the GUI.
     * @param inventory Inventory of the player
     * @param offsetX Offset to X
     * @param offsetY Offset to Y
     */
    protected void addPlayerInventory(PlayerInventory inventory, int offsetX, int offsetY) {
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
    protected void addPlayerArmorInventory(PlayerInventory inventory, int offsetX, int offsetY) {
        for (int k = 0; k < 4; ++k) {
            EquipmentSlotType equipmentSlot = EQUIPMENT_SLOTS[k];
            addSlot(new SlotArmor(inventory, 4 * 9 + (3 - k), offsetX, offsetY + k * ITEMBOX, inventory.player, equipmentSlot));
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
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
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

                if (slot.isItemValid(stack) && !existingStack.isEmpty() && existingStack.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
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
    public ItemStack slotClick(int slotId, int dragType, ClickType clickType, PlayerEntity player) {
        Slot slot = slotId < 0 ? null : this.inventorySlots.get(slotId);
        PlayerInventory inventoryplayer = player.inventory;
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

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, ClickType clickType, PlayerEntity player) {
        ItemStack stack = ItemStack.EMPTY;

        if (mouseButton == 2) {
            if (((SlotExtended) slot).isAdjustable()) {
                slot.putStack(ItemStack.EMPTY);
            }
        } else if (mouseButton == 0 || mouseButton == 1) {
            PlayerInventory playerInv = player.inventory;
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
                if (ItemMatch.areItemStacksEqual(stackSlot, stackHeld, ItemMatch.ITEM | ItemMatch.NBT)) {
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
    public void putButtonAction(String buttonId, IContainerButtonAction<ContainerExtended> action) {
        buttonActions.put(buttonId, action);
    }

    @Override
    public boolean onButtonClick(String buttonId) {
        IContainerButtonAction<ContainerExtended> action;
        if((action = buttonActions.get(buttonId)) != null) {
            action.onAction(buttonId, this);
            return true;
        }
        return false;
    }

    /**
     * @return The next unique value id.
     */
    protected int getNextValueId() {
        return nextValueId++;
    }

    @Override
    public void setValue(int valueId, CompoundNBT value) {
        if (!values.containsKey(valueId) || !values.get(valueId).equals(value)) {
            if (!player.getEntityWorld().isRemote()) { // server -> client
                CyclopsCore._instance.getPacketHandler().sendToPlayer(new ValueNotifyPacket(getType(), valueId, value), (ServerPlayerEntity) player);
            } else { // client -> server
                CyclopsCore._instance.getPacketHandler().sendToServer(new ValueNotifyPacket(getType(), valueId, value));
            }
            values.put(valueId, value);
        }
    }

    @Override
    public CompoundNBT getValue(int valueId) {
        return values.get(valueId);
    }

    @Override
    public Set<Integer> getValueIds() {
        return values.keySet();
    }

    @Override
    public void onUpdate(int valueId, CompoundNBT value) {
        values.put(valueId, value);
        if(guiValueListener != null) {
            guiValueListener.onUpdate(valueId, value);
        }
    }

    /**
     * Register the given variable for automatically sychronizing between client and server.
     *
     * This method should be called in the constructor of a container,
     * and the resulting supplier should be stored.
     * This resulting supplier can be called at any time by the client to lookup values.
     *
     * @param clazz The class of the variable to sync.
     * @param serverValueSupplier A supplier for the server-side variable value.
     * @param <T> The variable type.
     * @return A supplier that can be called for retrieving the value.
     */
    public <T> Supplier<T> registerSyncedVariable(Class<T> clazz, Supplier<T> serverValueSupplier) {
        SyncedGuiVariable<T> variable = new SyncedGuiVariable<>(this, clazz, serverValueSupplier);
        this.syncedGuiVariables.add(variable);
        return variable;
    }

}
