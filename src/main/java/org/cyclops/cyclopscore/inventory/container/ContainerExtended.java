package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.Level;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A container with inventory.
 * @author rubensworks
 */
public abstract class ContainerExtended extends AbstractContainerMenu implements IContainerButtonClickAcceptorServer<ContainerExtended>,
        IValueNotifier, IValueNotifiable {

    private static final EquipmentSlot[] EQUIPMENT_SLOTS = new EquipmentSlot[] {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    protected static final int ITEMBOX = 18;

    private final Map<String, IContainerButtonAction<ContainerExtended>> buttonActions = Maps.newHashMap();
    private final Map<Integer, CompoundTag> values = Maps.newHashMap();
    private final List<SyncedGuiVariable<?>> syncedGuiVariables = Lists.newArrayList();
    private int nextValueId = 0;
    private IValueNotifiable guiValueListener = null;

    private Inventory playerIInventory;
    protected final Player player;
    protected int offsetX = 0;
    protected int offsetY = 0;

    /* The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?) */
    private int quickcraftType = -1;
    /** The current drag event (0 : start, 1 : add slot : 2 : end) */
    private int quickcraftStatus;
    /** The list of slots where the itemstack holds will be distributed */
    private final Set<Slot> quickcraftSlots = Sets.<Slot>newHashSet();

    /**
     * Make a new ContainerExtended.
     * @param type The container type.
     * @param id The container id.
     * @param inventory The player inventory.
     */
    public ContainerExtended(@Nullable MenuType<?> type, int id, Inventory inventory) {
        super(type, id);
        this.playerIInventory = inventory;
        this.player = inventory.player;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!player.level().isClientSide()) {
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
    public void addSlotListener(ContainerListener listener) {
        super.addSlotListener(listener);
        if(!player.getCommandSenderWorld().isClientSide()) {
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

    protected Slot createNewSlot(Container inventory, int index, int x, int y) {
        return new Slot(inventory, index, x, y);
    }

    static void setSlotPos(Slot slot, String fieldName, int newValue) {
        try {
            Field field = ObfuscationReflectionHelper.findField(Slot.class, fieldName);
            field.setAccessible(true);
            field.set(slot, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setSlotPosX(Slot slot, int newValue) {
        setSlotPos(slot, "f_40220_", newValue);
    }

    public static void setSlotPosY(Slot slot, int newValue) {
        setSlotPos(slot, "f_40221_", newValue);
    }

    @Override
    protected Slot addSlot(Slot slot) {
        setSlotPosX(slot, slot.x + offsetX);
        setSlotPosY(slot, slot.y + offsetY);
        return super.addSlot(slot);
    }

    protected void addInventory(Container inventory, int indexOffset, int offsetX, int offsetY, int rows, int cols) {
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
    protected void addPlayerInventory(Inventory inventory, int offsetX, int offsetY) {
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
    protected void addPlayerArmorInventory(Inventory inventory, int offsetX, int offsetY) {
        for (int k = 0; k < 4; ++k) {
            EquipmentSlot equipmentSlot = EQUIPMENT_SLOTS[k];
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
    public ItemStack quickMoveStack(Player player, int slotID) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(slotID);
        int slots = getSizeInventory();

        if(slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem().copy();
            stack = stackInSlot.copy();

            if(slotID < slots) { // Click in tile -> player inventory
                if(!moveItemStackTo(stackInSlot, getSlotStart(slotID, slots, true), getSlotRange(slotID, this.slots.size(), true), true)) {
                    return ItemStack.EMPTY;
                }
            } else if(!moveItemStackTo(stackInSlot, getSlotStart(slotID, 0, false), getSlotRange(slotID, slots, false), false)) { // Click in player inventory -> tile
                return ItemStack.EMPTY;
            }

            if(stackInSlot.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.set(stackInSlot);
            }

            if(stackInSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return stack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int slotStart, int slotRange, boolean reverse) {
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
                slot = this.slots.get(slotIndex);
                int maxSlotSize = Math.min(slot.getMaxStackSize(), maxStack);
                existingStack = slot.getItem().copy();

                if (slot.mayPlace(stack) && !existingStack.isEmpty() && ItemStack.isSameItemSameTags(stack, existingStack)) {
                    int existingSize = existingStack.getCount() + stack.getCount();
                    if (existingSize <= maxSlotSize) {
                        stack.setCount(0);
                        existingStack.setCount(existingSize);
                        slot.set(existingStack);
                        successful = true;
                    } else if (existingStack.getCount() < maxSlotSize) {
                        stack.shrink(maxSlotSize - existingStack.getCount());
                        existingStack.setCount(maxSlotSize);
                        slot.set(existingStack);
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
                slot = this.slots.get(slotIndex);
                existingStack = slot.getItem().copy();

                if (slot.mayPlace(stack) && existingStack.isEmpty()) {
                    int placedAmount = Math.min(stack.getCount(), slot.getMaxStackSize());
                    ItemStack toPut = stack.copy();
                    toPut.setCount(placedAmount);
                    slot.set(toPut);
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
    public Inventory getPlayerIInventory() {
        return playerIInventory;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        Slot slot = slotId < 0 ? null : this.slots.get(slotId);
        // Copied and adjusted from net.minecraft.world.inventory.AbstractContainerMenu
        Inventory inventory = player.getInventory();
        if (clickType == ClickType.QUICK_CRAFT) {
            int i = this.quickcraftStatus;
            this.quickcraftStatus = getQuickcraftHeader(dragType);
            if ((i != 1 || this.quickcraftStatus != 2) && i != this.quickcraftStatus) {
                this.resetQuickCraft();
            } else if (this.getCarried().isEmpty()) {
                this.resetQuickCraft();
            } else if (this.quickcraftStatus == 0) {
                this.quickcraftType = getQuickcraftType(dragType);
                if (isValidQuickcraftType(this.quickcraftType, player)) {
                    this.quickcraftStatus = 1;
                    this.quickcraftSlots.clear();
                } else {
                    this.resetQuickCraft();
                }
            } else if (this.quickcraftStatus == 1) {
                // Slot slot = this.slots.get(slotId);
                ItemStack itemstack = this.getCarried();
                if (canItemQuickReplace(slot, itemstack, true) && slot.mayPlace(itemstack) && (this.quickcraftType == 2 || itemstack.getCount() > this.quickcraftSlots.size()) && this.canDragTo(slot)) {
                    this.quickcraftSlots.add(slot);
                }
            } else if (this.quickcraftStatus == 2) {
                if (!this.quickcraftSlots.isEmpty()) {
                    int phantomCount = 0; // Added

                    if (this.quickcraftSlots.size() == 1) {
                        int l = (this.quickcraftSlots.iterator().next()).index;
                        this.resetQuickCraft();
                        this.clicked(l, this.quickcraftType, ClickType.PICKUP, player); // changed from doClick to clicked
                        return;
                    }

                    ItemStack itemstack2 = this.getCarried().copy();
                    if (itemstack2.isEmpty()) {
                        this.resetQuickCraft();
                        return;
                    }

                    int k1 = this.getCarried().getCount();

                    for(Slot slot1 : this.quickcraftSlots) {
                        ItemStack itemstack1 = this.getCarried();
                        if (slot1 != null && canItemQuickReplace(slot1, itemstack1, true) && slot1.mayPlace(itemstack1) && (this.quickcraftType == 2 || itemstack1.getCount() >= this.quickcraftSlots.size()) && this.canDragTo(slot1)) {
                            int j = slot1.hasItem() ? slot1.getItem().getCount() : 0;
                            int k = Math.min(itemstack2.getMaxStackSize(), slot1.getMaxStackSize(itemstack2));
                            int l = Math.min(getQuickCraftPlaceCount(this.quickcraftSlots, this.quickcraftType, itemstack2) + j, k);
                            k1 -= l - j;
                            slot1.setByPlayer(itemstack2.copyWithCount(l));

                            // --- Added ---
                            if (slot1 instanceof SlotExtended && ((SlotExtended) slot1).isPhantom()) {
                                phantomCount += l - j;
                            }
                        }
                    }

                    itemstack2.setCount(k1 + phantomCount); // Changed
                    this.setCarried(itemstack2);
                }

                this.resetQuickCraft();
            } else {
                this.resetQuickCraft();
            }
        } else if (this.quickcraftStatus != 0) {
            this.resetQuickCraft();
        } else if (slot instanceof SlotExtended && ((SlotExtended) slot).isPhantom()) { // Phantom slot logic added
            slotClickPhantom(slot, dragType, clickType, player);
        } else {
            // All other cases are delegated to the original code
            super.clicked(slotId, dragType, clickType, player);
        }
    }

    @Override
    protected void resetQuickCraft() {
        super.resetQuickCraft();
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
    }

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, ClickType clickType, Player player) {
        ItemStack stack = ItemStack.EMPTY;

        if (mouseButton == 2) {
            if (((SlotExtended) slot).isAdjustable()) {
                slot.set(ItemStack.EMPTY);
            }
        } else if (mouseButton == 0 || mouseButton == 1) {
            slot.setChanged();
            ItemStack stackSlot = slot.getItem();
            ItemStack stackHeld = this.getCarried();

            if (!stackSlot.isEmpty()) {
                stack = stackSlot.copy();
            }

            if (stackSlot.isEmpty()) {
                if (!stackHeld.isEmpty() && slot.mayPlace(stackHeld)) {
                    fillPhantomSlot(slot, stackHeld, mouseButton, clickType);
                }
            } else if (stackHeld.isEmpty()) {
                adjustPhantomSlot(slot, mouseButton, clickType);
                slot.onTake(player, this.getCarried());
            } else if (slot.mayPlace(stackHeld)) {
                if (ItemMatch.areItemStacksEqual(stackSlot, stackHeld, ItemMatch.ITEM | ItemMatch.TAG)) {
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
        ItemStack stackSlot = slot.getItem();
        int stackSize;
        if (clickType == ClickType.QUICK_MOVE) {
            stackSize = mouseButton == 0 ? (stackSlot.getCount() + 1) / 2 : stackSlot.getCount() * 2;
        } else {
            stackSize = mouseButton == 0 ? stackSlot.getCount() - 1 : stackSlot.getCount() + 1;
        }

        if (stackSize > slot.getMaxStackSize()) {
            stackSize = slot.getMaxStackSize();
        }

        stackSlot.setCount(stackSize);

        if (stackSlot.getCount() <= 0) {
            slot.set(ItemStack.EMPTY);
        }
    }

    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, ClickType clickType) {
        if (!((SlotExtended) slot).isAdjustable()) {
            return;
        }
        int stackSize = mouseButton == 0 ? stackHeld.getCount() : 1;
        if (stackSize > slot.getMaxStackSize()) {
            stackSize = slot.getMaxStackSize();
        }
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.setCount(stackSize);

        slot.set(phantomStack);
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
    public void setValue(int valueId, CompoundTag value) {
        if (!values.containsKey(valueId) || !values.get(valueId).equals(value)) {
            try {
                if (!player.level().isClientSide()) { // server -> client
                    CyclopsCore._instance.getPacketHandler().sendToPlayer(new ValueNotifyPacket(getType(), valueId, value), (ServerPlayer) player);
                } else { // client -> server
                    CyclopsCore._instance.getPacketHandler().sendToServer(new ValueNotifyPacket(getType(), valueId, value));
                }
                values.put(valueId, value);
            } catch (EncoderException e) {
                CyclopsCore.clog(Level.WARN, e.getMessage());
            }
        }
    }

    @Override
    public CompoundTag getValue(int valueId) {
        return values.get(valueId);
    }

    @Override
    public Set<Integer> getValueIds() {
        return values.keySet();
    }

    @Override
    public MenuType<?> getValueNotifiableType() {
        return getType();
    }

    @Override
    public void onUpdate(int valueId, CompoundTag value) {
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
