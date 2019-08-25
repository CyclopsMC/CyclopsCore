package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack == null) return false;
        // return itemStack.getItem().isValidArmor(itemStack, armorType, player); // TODO
        return itemStack.getItem() instanceof ArmorItem;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getSlotTexture() {
        return PlayerContainer.ARMOR_SLOT_TEXTURES[armorType.getIndex()];
    }
    
}
