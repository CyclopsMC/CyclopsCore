package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Slot that is used to hold armor.
 * @author rubensworks
 *
 */
public class SlotArmor extends Slot {

    private final int armorIndex;
    private final EntityPlayer player;

    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param player The player entity.
     * @param armorIndex The index of the armor slot.
     */
    public SlotArmor(IInventory inventory, int index, int x,
                     int y, EntityPlayer player, int armorIndex) {
        super(inventory, index, x, y);
        this.armorIndex = armorIndex;
        this.player = player;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem().isValidArmor(itemStack, armorIndex, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getSlotTexture()
    {
        return ItemArmor.EMPTY_SLOT_NAMES[armorIndex];
    }
    
}
