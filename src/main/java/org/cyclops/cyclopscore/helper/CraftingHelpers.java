package org.cyclops.cyclopscore.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    private static final LoadingCache<Pair<CacheableInventoryCrafting, Integer>, Optional<IRecipe>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Pair<CacheableInventoryCrafting, Integer>, Optional<IRecipe>>() {
                @Override
                public Optional<IRecipe> load(Pair<CacheableInventoryCrafting, Integer> key) throws Exception {
                    IRecipe recipe = CraftingManager.findMatchingRecipe(key.getLeft().getInventoryCrafting(), DimensionManager.getWorld(key.getRight()));
                    return Optional.ofNullable(recipe);
                }
            });

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

    /**
     * A cache-based variant of {@link CraftingManager#findMatchingRecipe(InventoryCrafting, World)}.
     * @param inventoryCrafting The crafting inventory.
     * @param world The world.
     * @param uniqueInventory If inventoryCrafting is a unique instance that can be cached safely.
     *                        Otherwise a deep copy will be taken.
     * @return The recipe or null.
     */
    public static IRecipe findMatchingRecipeCached(InventoryCrafting inventoryCrafting, World world, boolean uniqueInventory) {
        return CACHE_RECIPES.getUnchecked(Pair.of(new CacheableInventoryCrafting(inventoryCrafting, !uniqueInventory), world.provider.getDimension()))
                .orElse(null);
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
                output.getItem().getRegistryName().getPath());
        int counter = 10;
        while (ForgeRegistries.RECIPES.containsKey(id)) {
            id = new ResourceLocation(id.getNamespace(), output.getItem().getRegistryName().getPath() + "_" + ++counter);
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

    public static class CacheableInventoryCrafting {

        private final InventoryCrafting inventoryCrafting;

        public CacheableInventoryCrafting(InventoryCrafting inventoryCrafting, boolean copyInventory) {
            if (copyInventory) {
                // Deep-copy of the inventory to enable caching
                this.inventoryCrafting = new InventoryCrafting(new Container() {
                    @Override
                    public boolean canInteractWith(EntityPlayer playerIn) {
                        return false;
                    }
                }, inventoryCrafting.getWidth(), inventoryCrafting.getHeight());
                for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
                    this.inventoryCrafting.setInventorySlotContents(i, inventoryCrafting.getStackInSlot(i).copy());
                }
            } else {
                this.inventoryCrafting = inventoryCrafting;
            }
        }

        public InventoryCrafting getInventoryCrafting() {
            return inventoryCrafting;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CacheableInventoryCrafting)) {
                return false;
            }
            for (int i = 0; i < getInventoryCrafting().getSizeInventory(); i++) {
                if (!ItemStack.areItemStacksEqual(getInventoryCrafting().getStackInSlot(i),
                        ((CacheableInventoryCrafting) obj).getInventoryCrafting().getStackInSlot(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 11 + getInventoryCrafting().getSizeInventory();
            for (int i = 0; i < getInventoryCrafting().getSizeInventory(); i++) {
                hash = hash << 1;
                hash |= ItemStackHelpers.getItemStackHashCode(getInventoryCrafting().getStackInSlot(i));
            }
            return hash;
        }

    }

}
