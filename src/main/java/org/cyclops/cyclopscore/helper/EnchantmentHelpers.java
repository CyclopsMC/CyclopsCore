package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.common.CommonHooks;

/**
 * This class contains helper methods to get and set certain enchants
 * and enchantment levels on item stacks.
 * @author immortaleeb
 *
 */
public class EnchantmentHelpers {

    /**
     * Checks if an itemStack has a certain enchantment.
     * @param itemStack The itemStack to check.
     * @param enchantment The Enchantment to compare.
     * @return If it applies.
     */
    public static boolean doesEnchantApply(ItemStack itemStack, Holder<Enchantment> enchantment) {
        return getEnchantmentLevel(itemStack, enchantment) > 0;
    }

    /**
     * Returns the level of an enchantment given an itemStack.
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantment The Enchantment to compare.
     * @return The level of the enchantment on the given item
     */
    public static int getEnchantmentLevel(ItemStack itemStack, Holder<Enchantment> enchantment) {
        ItemEnchantments enchantments = itemStack.getAllEnchantments(CommonHooks.resolveLookup(Registries.ENCHANTMENT));
        return enchantments.getLevel(enchantment);
    }

    public static DataComponentType<ItemEnchantments> getComponentType(ItemStack itemStack) {
        return itemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
    }

    /**
     * Sets the level of an enchantment given an itemStack.
     * Will clear the enchantment if the new level &lt;= 0
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantment The Enchantment to compare.
     * @param level The new level of the enchantment on the given item
     */
    public static void setEnchantmentLevel(ItemStack itemStack, Holder<Enchantment> enchantment, int level) {
        DataComponentType<ItemEnchantments> datacomponenttype = getComponentType(itemStack);
        ItemEnchantments enchantments = itemStack.get(datacomponenttype);
        ItemEnchantments.Mutable enchantmentsMutable = new ItemEnchantments.Mutable(enchantments);
        enchantmentsMutable.set(enchantment, level);
        itemStack.set(datacomponenttype, enchantmentsMutable.toImmutable());
    }
}
