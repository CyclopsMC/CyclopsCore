package org.cyclops.cyclopscore.helper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

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
     * @return The id of the enchantment in the enchantmentlist or -1 if it does not apply.
     */
    public static int doesEnchantApply(ItemStack itemStack, Enchantment enchantment) {
        ListTag enchantmentList = itemStack.getEnchantmentTags();
        for(int i = 0; i < enchantmentList.size(); i++) {
            if (BuiltInRegistries.ENCHANTMENT.getKey(enchantment).equals(new ResourceLocation(enchantmentList.getCompound(i).getString("id")))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the level of an enchantment given an itemStack and the list id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @return The level of the enchantment on the given item
     */
    public static int getEnchantmentLevel(ItemStack itemStack, int enchantmentListID) {
        ListTag enchlist = itemStack.getEnchantmentTags();
        return enchlist.getCompound(enchantmentListID).getShort("lvl");
    }

    /**
     * Returns the enchantment given an itemStack and the list id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @return The enchantment on the given item
     */
    public static Enchantment getEnchantment(ItemStack itemStack, int enchantmentListID) {
        ListTag enchlist = itemStack.getEnchantmentTags();
        return BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(
                enchlist.getCompound(enchantmentListID).getString("id")));
    }

    /**
     * Sets the level of an enchantment given an itemStack and the id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * Will clear the enchantment if the new level &lt;= 0
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantmentListID The id of the enchantment in the enchantment list
     * @param level The new level of the enchantment on the given item
     */
    public static void setEnchantmentLevel(ItemStack itemStack, int enchantmentListID, int level) {
        ListTag enchlist = itemStack.getEnchantmentTags();
        if(level <= 0) {
            enchlist.remove(enchantmentListID);
            if(enchlist.size() == 0) {
                itemStack.getTag().remove("Enchantments");
            }
        } else {
            CompoundTag compound = enchlist.getCompound(enchantmentListID);
            compound.putShort("lvl", (short) level);
        }
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put("Enchantments", enchlist);
    }

    /**
     * Sets the level of an enchantment given an itemStack and the id
     * of the enchantment in the enchantmentlist (see doesEnchantApply() to get
     * the id in the enchantmentlist)
     * Will clear the enchantment if the new level &lt;= 0
     * @param itemStack The itemStack which contains the enchanted item
     * @param enchantment The enchantment
     * @param level The new level of the enchantment on the given item
     */
    public static void setEnchantmentLevel(ItemStack itemStack, Enchantment enchantment, int level) {
        int existingIndex;
        if((existingIndex = doesEnchantApply(itemStack, enchantment)) >= 0) {
            setEnchantmentLevel(itemStack, existingIndex, level);
        } else {
            itemStack.enchant(enchantment, level);
        }
    }

}
