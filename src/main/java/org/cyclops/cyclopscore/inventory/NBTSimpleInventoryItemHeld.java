package org.cyclops.cyclopscore.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        InventoryHelpers.validateNBTStorage(this, itemLocation.getItemStack(player), this.tagName);
    }

    @Override
    public void setChanged() {
        ItemStack itemStack = itemLocation.getItemStack(player);
        CompoundTag tag = itemStack.getOrCreateTag();
        writeToNBT(tag, this.tagName);
        itemStack.setTag(tag);
    }

    @Override
    public void readFromNBT(CompoundTag data, String tagName) {
        InventoryHelpers.readFromNBT(this, data, tagName);
    }

    @Override
    public void writeToNBT(CompoundTag data, String tagName) {
        InventoryHelpers.writeToNBT(this, data, tagName);
    }

}
