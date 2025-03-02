package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Slot that is used to hold armor.
 * @author rubensworks
 *
 */
public class SlotArmor extends Slot {

    private final EquipmentSlot armorType;
    private final Player player;

    /**
     * Make a new instance.
     * @param inventory The inventory this slot will be in.
     * @param index The index of this slot.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param player The player entity.
     * @param armorType The armor type.
     */
    public SlotArmor(Container inventory, int index, int x,
                     int y, Player player, EquipmentSlot armorType) {
        super(inventory, index, x, y);
        this.armorType = armorType;
        this.player = player;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return this.armorType == this.player.getEquipmentSlotForItem(itemStack);
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack itemstack = this.getItem();
        return !itemstack.isEmpty() && !player.isCreative() && EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) ? false : super.mayPickup(player);
    }

    @Override
    public ResourceLocation getNoItemIcon() {
        return InventoryMenu.TEXTURE_EMPTY_SLOTS.get(armorType);
    }
}
