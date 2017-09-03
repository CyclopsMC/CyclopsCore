package org.cyclops.cyclopscore.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    public static IRecipe findCraftingRecipe(ItemStack itemStack, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(IRecipe recipe : CraftingManager.REGISTRY) {
            if(itemStacksEqual(recipe.getRecipeOutput(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find crafting recipe for " + itemStack + "::"
                + itemStack.getTagCompound() + " with index " + index);
    }

    public static Map.Entry<ItemStack, ItemStack> findFurnaceRecipe(ItemStack itemStack, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(Map.Entry<ItemStack, ItemStack> recipe : (FurnaceRecipes.instance().
                getSmeltingList()).entrySet()) {
            if(itemStacksEqual(recipe.getValue(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find furnace recipe for " + itemStack + "::"
                + itemStack.getTagCompound() + " with index " + index);
    }

    public static ResourceLocation newRecipeIdentifier(ItemStack output) {
        ResourceLocation id = new ResourceLocation(Loader.instance().activeModContainer().getModId(),
                output.getItem().getRegistryName().getResourcePath());
        int counter = 10;
        while (ForgeRegistries.RECIPES.containsKey(id)) {
            id = new ResourceLocation(id.getResourceDomain(), output.getItem().getRegistryName().getResourcePath() + "_" + ++counter);
        }
        return id;
    }

    /**
     * Register a crafting recipe.
     * @param id The recipe id
     * @param recipe The recipe
     * @return The recipe
     */
    public static IRecipe registerRecipe(ResourceLocation id, IRecipe recipe) {
        recipe.setRegistryName(id);
        ForgeRegistries.RECIPES.register(recipe);
        return recipe;
    }

    public static boolean itemStacksEqual(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1 != null && itemStack2 != null &&
               itemStack1.getItem() == itemStack2.getItem() &&
               ItemStack.areItemStackTagsEqual(itemStack1, itemStack2) &&
               (itemStack1.getItemDamage() == itemStack2.getItemDamage() ||
                       itemStack1.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                       itemStack2.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
                       itemStack1.getItem().isDamageable());
    }

}
