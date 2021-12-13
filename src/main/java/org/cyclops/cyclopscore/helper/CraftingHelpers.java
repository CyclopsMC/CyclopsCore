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
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Several convenience functions for crafting.
 * @author rubensworks
 */
public class CraftingHelpers {

    private static RecipeManager CLIENT_RECIPE_MANAGER;

    public static void load() {
        MinecraftForge.EVENT_BUS.register(CraftingHelpers.class);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesLoaded(RecipesUpdatedEvent event) {
        CLIENT_RECIPE_MANAGER = event.getRecipeManager();
    }

    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> findRecipes(World world, IRecipeType<? extends T> recipeType) {
        return world.isClientSide() ? getClientRecipes(recipeType) : findServerRecipes((ServerWorld) world, recipeType);
    }

    public static RecipeManager getRecipeManager() {
        return MinecraftHelpers.isClientSide()
                ? (CLIENT_RECIPE_MANAGER != null ? CLIENT_RECIPE_MANAGER : Minecraft.getInstance().getConnection().getRecipeManager())
                : Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD), "Server is still loading").getRecipeManager();
    }

    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> getServerRecipe(IRecipeType<? extends T> recipeType, ResourceLocation recipeName) {
        return (Optional<T>) Optional.ofNullable(getRecipeManager().byType(recipeType).get(recipeName));
    }

    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> findServerRecipe(IRecipeType<T> recipeType, C container, World world) {
        return world.getRecipeManager().getRecipeFor(recipeType, container, world);
    }

    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> findServerRecipes(IRecipeType<? extends T> recipeType) {
        return findServerRecipes(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD)), recipeType);
    }

    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> findServerRecipes(ServerWorld world, IRecipeType<? extends T> recipeType) {
        return (Collection<T>) world.getRecipeManager().byType(recipeType).values();
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> Optional<T> getClientRecipe(IRecipeType<? extends T> recipeType, ResourceLocation recipeName) {
        return (Optional<T>) Optional.ofNullable(getRecipeManager().byType(recipeType).get(recipeName));
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> Collection<T> getClientRecipes(IRecipeType<? extends T> recipeType) {
        return (Collection<T>) getRecipeManager().byType(recipeType).values();
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends IInventory, T extends IRecipe<C>> T findClientRecipe(ItemStack itemStack, IRecipeType<T> recipeType, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(T recipe : getClientRecipes(recipeType)) {
            if(ItemStack.isSame(recipe.getResultItem(), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find recipe for " + itemStack + "::"
                + itemStack.getTag() + " with index " + index);
    }

    private static final LoadingCache<Triple<IRecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<IRecipe>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Triple<IRecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<IRecipe>>() {
                @Override
                public Optional<IRecipe> load(Triple<IRecipeType<?>, CacheableCraftingInventory, ResourceLocation> key) throws Exception {
                    ServerWorld world = ServerLifecycleHooks.getCurrentServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, key.getRight()));
                    return world.getRecipeManager().getRecipeFor((IRecipeType) key.getLeft(), key.getMiddle().getInventoryCrafting(), world);
                }
            });

    /**
     * A cache-based variant of {@link net.minecraft.item.crafting.RecipeManager#getRecipeFor(IRecipeType, IInventory, World)}.
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
                new CacheableCraftingInventory(inventoryCrafting, !uniqueInventory), world.dimension().getRegistryName()));
    }

    public static class CacheableCraftingInventory {

        private final IInventory inventoryCrafting;

        public CacheableCraftingInventory(IInventory inventoryCrafting, boolean copyInventory) {
            if (copyInventory) {
                // Deep-copy of the inventory to enable caching
                this.inventoryCrafting = new CraftingInventory(new Container(null, 0) {
                    @Override
                    public boolean stillValid(PlayerEntity playerIn) {
                        return false;
                    }
                }, inventoryCrafting.getContainerSize(), 1);
                for (int i = 0; i < inventoryCrafting.getContainerSize(); i++) {
                    this.inventoryCrafting.setItem(i, inventoryCrafting.getItem(i).copy());
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
            for (int i = 0; i < getInventoryCrafting().getContainerSize(); i++) {
                if (!ItemStack.isSame(getInventoryCrafting().getItem(i),
                        ((CacheableCraftingInventory) obj).getInventoryCrafting().getItem(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 11 + getInventoryCrafting().getContainerSize();
            for (int i = 0; i < getInventoryCrafting().getContainerSize(); i++) {
                hash = hash << 1;
                hash |= ItemStackHelpers.getItemStackHashCode(getInventoryCrafting().getItem(i));
            }
            return hash;
        }

    }

}
