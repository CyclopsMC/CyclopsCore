package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

/**
 * Slot that is used to hold armor.
 * @author rubensworks
 *
 */
public class SlotArmor extends Slot {

    private final EquipmentSlotType armorType;
    private final PlayerEntity player;

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
                     int y, PlayerEntity player, EquipmentSlotType armorType) {
        super(inventory, index, x, y);
        this.armorType = armorType;
        this.player = player;
        setBackground(PlayerContainer.BLOCK_ATLAS, PlayerContainer.TEXTURE_EMPTY_SLOTS[armorType.getIndex()]);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getEquipmentSlot() == armorType
                || (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getSlot() == armorType);
    }
    
}
