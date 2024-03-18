package org.cyclops.cyclopscore.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
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
        NeoForge.EVENT_BUS.register(CraftingHelpers.class);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesLoaded(RecipesUpdatedEvent event) {
        CLIENT_RECIPE_MANAGER = event.getRecipeManager();
    }

    public static <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(Level world, RecipeType<? extends T> recipeType) {
        return world.isClientSide() ? getClientRecipes(recipeType) : findServerRecipes((ServerLevel) world, recipeType);
    }

    public static RecipeManager getRecipeManager() {
        return MinecraftHelpers.isClientSide()
                ? (CLIENT_RECIPE_MANAGER != null ? CLIENT_RECIPE_MANAGER : Minecraft.getInstance().getConnection().getRecipeManager())
                : Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD), "Server is still loading").getRecipeManager();
    }

    public static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> getServerRecipe(RecipeType<T> recipeType, ResourceLocation recipeName) {
        return Optional.ofNullable(getRecipeManager().byType(recipeType).get(recipeName));
    }

    public static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> findServerRecipe(RecipeType<T> recipeType, C container, Level world) {
        return world.getRecipeManager().getRecipeFor(recipeType, container, world);
    }

    public static <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(RecipeType<? extends T> recipeType) {
        return findServerRecipes(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD)), recipeType);
    }

    public static <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(ServerLevel world, RecipeType<? extends T> recipeType) {
        return (List<RecipeHolder<T>>) (List) world.getRecipeManager().getAllRecipesFor(recipeType);
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> getClientRecipe(RecipeType<T> recipeType, ResourceLocation recipeName) {
        return Optional.ofNullable(getRecipeManager().byType(recipeType).get(recipeName));
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends Container, T extends Recipe<C>> List<RecipeHolder<T>> getClientRecipes(RecipeType<? extends T> recipeType) {
        return (List<RecipeHolder<T>>) (List)  getRecipeManager().getAllRecipesFor(recipeType);
    }

    @OnlyIn(Dist.CLIENT)
    public static <C extends Container, T extends Recipe<C>> RecipeHolder<T> findClientRecipe(RegistryAccess registryAccess, ItemStack itemStack, RecipeType<T> recipeType, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(RecipeHolder<T> recipe : getClientRecipes(recipeType)) {
            if(ItemStack.isSameItemSameTags(recipe.value().getResultItem(registryAccess), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find recipe for " + itemStack + "::"
                + itemStack.getTag() + " with index " + index);
    }

    private static final LoadingCache<Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<RecipeHolder<? extends Recipe>>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<RecipeHolder<? extends Recipe>>>() {
                @Override
                public Optional<RecipeHolder<? extends Recipe>> load(Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation> key) throws Exception {
                    ServerLevel world = ServerLifecycleHooks.getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, key.getRight()));
                    return world.getRecipeManager().getRecipeFor((RecipeType) key.getLeft(), key.getMiddle().getInventoryCrafting(), world);
                }
            });

    /**
     * A cache-based variant of {@link net.minecraft.world.item.crafting.RecipeManager#getRecipeFor(RecipeType, Container, Level)}.
     * @param recipeType The recipe type.
     * @param inventoryCrafting The crafting inventory.
     * @param world The world.
     * @param uniqueInventory If inventoryCrafting is a unique instance that can be cached safely.
     *                        Otherwise a deep copy will be taken.
     * @return The optional recipe if one was found.
     * @param <C> The inventory type.
     * @param <T> The recipe type.
     */
    public static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipeCached(RecipeType<T> recipeType,
                                                                                            C inventoryCrafting,
                                                                                            Level world, boolean uniqueInventory) {
        return (Optional) CACHE_RECIPES.getUnchecked(Triple.of(recipeType,
                new CacheableCraftingInventory(inventoryCrafting, !uniqueInventory), world.dimension().location()));
    }

    public static class CacheableCraftingInventory {

        private final Container inventoryCrafting;

        public CacheableCraftingInventory(Container inventoryCrafting, boolean copyInventory) {
            if (copyInventory) {
                // Deep-copy of the inventory to enable caching
                int width = inventoryCrafting.getContainerSize();
                int height = 1;
                if (inventoryCrafting instanceof CraftingContainer) {
                    width = ((CraftingContainer) inventoryCrafting).getWidth();
                    height = ((CraftingContainer) inventoryCrafting).getHeight();
                }
                this.inventoryCrafting = new TransientCraftingContainer(new AbstractContainerMenu(null, 0) {
                    @Override
                    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
                        return ItemStack.EMPTY;
                    }

                    @Override
                    public boolean stillValid(Player playerIn) {
                        return false;
                    }
                }, width, height);
                for (int i = 0; i < inventoryCrafting.getContainerSize(); i++) {
                    this.inventoryCrafting.setItem(i, inventoryCrafting.getItem(i).copy());
                }
            } else {
                this.inventoryCrafting = inventoryCrafting;
            }
        }

        public Container getInventoryCrafting() {
            return inventoryCrafting;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CacheableCraftingInventory)) {
                return false;
            }
            for (int i = 0; i < getInventoryCrafting().getContainerSize(); i++) {
                if (!ItemStack.isSameItemSameTags(getInventoryCrafting().getItem(i),
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
