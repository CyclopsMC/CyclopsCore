package org.cyclops.cyclopscore.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> findServerRecipe(IRecipeType<T> recipeType, C container, World world) {
        return world.getRecipeManager().getRecipe(recipeType, container, world);
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> getClientRecipe(IRecipeType<T> recipeType, ResourceLocation recipeName) {
        return (Optional<T>) Optional.ofNullable(Minecraft.getInstance().getConnection().getRecipeManager().getRecipes(recipeType).get(recipeName));
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> getClientRecipes(IRecipeType<T> recipeType) {
        return (Collection<T>) Minecraft.getInstance().getConnection().getRecipeManager().getRecipes(recipeType).values();
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> T findClientRecipe(ItemStack itemStack, IRecipeType<T> recipeType, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(T recipe : getClientRecipes(recipeType)) {
            if(ItemStack.areItemStacksEqual(recipe.getRecipeOutput(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find recipe for " + itemStack + "::"
                + itemStack.getTag() + " with index " + index);
    }

    private static final LoadingCache<Triple<IRecipeType<?>, CacheableCraftingInventory, DimensionType>, Optional<IRecipe>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Triple<IRecipeType<?>, CacheableCraftingInventory, DimensionType>, Optional<IRecipe>>() {
                @Override
                public Optional<IRecipe> load(Triple<IRecipeType<?>, CacheableCraftingInventory, DimensionType> key) throws Exception {
                    ServerWorld world = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), key.getRight(), false, false);
                    return world.getRecipeManager().getRecipe((IRecipeType) key.getLeft(), key.getMiddle().getInventoryCrafting(), world);
                }
            });

    /**
     * A cache-based variant of {@link net.minecraft.item.crafting.RecipeManager#getRecipe(IRecipeType, IInventory, World)}.
     * @param recipeType The recipe type.
     * @param inventoryCrafting The crafting inventory.
     * @param world The world.
     * @param uniqueInventory If inventoryCrafting is a unique instance that can be cached safely.
     *                        Otherwise a deep copy will be taken.
     * @return The optional recipe if one was found.
     * @param <C> The inventory type.
     * @param <T> The recipe type.
     */
    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> findRecipeCached(IRecipeType<T> recipeType,
                                                                                            C inventoryCrafting,
                                                                                            World world, boolean uniqueInventory) {
        return (Optional) CACHE_RECIPES.getUnchecked(Triple.of(recipeType,
                new CacheableCraftingInventory(inventoryCrafting, !uniqueInventory), world.getDimension().getType()));
    }

    public static class CacheableCraftingInventory {

        private final IInventory inventoryCrafting;

        public CacheableCraftingInventory(IInventory inventoryCrafting, boolean copyInventory) {
            if (copyInventory) {
                // Deep-copy of the inventory to enable caching
                this.inventoryCrafting = new CraftingInventory(new Container(null, 0) {
                    @Override
                    public boolean canInteractWith(PlayerEntity playerIn) {
                        return false;
                    }
                }, inventoryCrafting.getSizeInventory(), 1);
                for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
                    this.inventoryCrafting.setInventorySlotContents(i, inventoryCrafting.getStackInSlot(i).copy());
                }
            } else {
                this.inventoryCrafting = inventoryCrafting;
            }
        }

        public IInventory getInventoryCrafting() {
            return inventoryCrafting;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CacheableCraftingInventory)) {
                return false;
            }
            for (int i = 0; i < getInventoryCrafting().getSizeInventory(); i++) {
                if (!ItemStack.areItemStacksEqual(getInventoryCrafting().getStackInSlot(i),
                        ((CacheableCraftingInventory) obj).getInventoryCrafting().getStackInSlot(i))) {
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
