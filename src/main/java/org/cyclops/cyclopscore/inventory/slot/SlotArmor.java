package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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

    private final EntityEquipmentSlot armorType;
    private final EntityPlayer player;

    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param player The player entity.
     * @param armorType The armor type.
     */
    public SlotArmor(IInventory inventory, int index, int x,
                     int y, EntityPlayer player, EntityEquipmentSlot armorType) {
        super(inventory, index, x, y);
        this.armorType = armorType;
        this.player = player;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem().isValidArmor(itemStack, armorType, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getSlotTexture()
    {
        return ItemArmor.EMPTY_SLOT_NAMES[armorType.getIndex()];
    }
    
}
