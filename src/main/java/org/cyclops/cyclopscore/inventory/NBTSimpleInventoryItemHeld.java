package org.cyclops.cyclopscore.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.helper.InventoryHelpers;

/**
 * A simple inventory for a currently held item by a player that can be stored in NBT.
 * @author rubensworks
 *
 */
public class NBTSimpleInventoryItemHeld extends SimpleInventory {

    protected final Player player;
    protected final ItemLocation itemLocation;
    protected final String tagName;

    /**
     * Make a new instance.
     * @param player The player holding the item.
     * @param itemLocation The item location.
     * @param size The amount of slots in the inventory.
     * @param stackLimit The stack limit for each slot.
     * @param tagName The NBT tag name to store this inventory in.
     *                This should be the same tag name that is used to call the NBT read/write methods.
     */
    public NBTSimpleInventoryItemHeld(Player player, ItemLocation itemLocation, int size, int stackLimit, String tagName) {
        super(size, stackLimit);
        this.player = player;
        this.itemLocation = itemLocation;
        this.tagName = tagName;
    }

    @Override
    public void setChanged() {
        ItemStack itemStack = itemLocation.getItemStack(player);
        itemStack.set(RegistryEntries.COMPONENT_INVENTORY, this);
    }

    @Override
    public void readFromNBT(HolderLookup.Provider provider, CompoundTag data, String tagName) {
        InventoryHelpers.readFromNBT(provider, this, data, tagName);
    }

    @Override
    public void writeToNBT(HolderLookup.Provider provider, CompoundTag data, String tagName) {
        InventoryHelpers.writeToNBT(provider, this, data, tagName);
    }

}
