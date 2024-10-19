package org.cyclops.cyclopscore.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author rubensworks
 */
public class CraftingHelpersCommon implements ICraftingHelpers {

    private final IModHelpers modHelpers;
    private final LoadingCache<Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<RecipeHolder<? extends Recipe>>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation>, Optional<RecipeHolder<? extends Recipe>>>() {
                @Override
                public Optional<RecipeHolder<? extends Recipe>> load(Triple<RecipeType<?>, CacheableCraftingInventory, ResourceLocation> key) throws Exception {
                    ServerLevel world = modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, key.getRight()));
                    return world.getRecipeManager().getRecipeFor((RecipeType) key.getLeft(), key.getMiddle().getInventoryCrafting(), world);
                }
            });

    public CraftingHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(Level world, RecipeType<? extends T> recipeType) {
        return world.isClientSide() ? getClientRecipes(recipeType) : findServerRecipes((ServerLevel) world, recipeType);
    }

    @Override
    public RecipeManager getRecipeManager() {
        return modHelpers.getMinecraftHelpers().isClientSide()
                ? (Minecraft.getInstance().getConnection().getRecipeManager())
                : Objects.requireNonNull(modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(Level.OVERWORLD), "Server is still loading").getRecipeManager();
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getServerRecipe(RecipeType<T> recipeType, ResourceLocation recipeName) {
        return Optional.ofNullable(getRecipeManager().byKeyTyped(recipeType, recipeName));
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findServerRecipe(RecipeType<T> recipeType, C container, Level world) {
        return world.getRecipeManager().getRecipeFor(recipeType, container, world);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(RecipeType<? extends T> recipeType) {
        return findServerRecipes(Objects.requireNonNull(modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(Level.OVERWORLD)), recipeType);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findServerRecipes(ServerLevel world, RecipeType<? extends T> recipeType) {
        return (List<RecipeHolder<T>>) (List) world.getRecipeManager().getAllRecipesFor(recipeType);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getClientRecipe(RecipeType<T> recipeType, ResourceLocation recipeName) {
        return Optional.ofNullable(getRecipeManager().byKeyTyped(recipeType, recipeName));
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getClientRecipes(RecipeType<? extends T> recipeType) {
        return (List<RecipeHolder<T>>) (List)  getRecipeManager().getAllRecipesFor(recipeType);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> RecipeHolder<T> findClientRecipe(RegistryAccess registryAccess, ItemStack itemStack, RecipeType<T> recipeType, int index) throws IllegalArgumentException {
        int indexAttempt = index;
        for(RecipeHolder<T> recipe : getClientRecipes(recipeType)) {
            if(ItemStack.isSameItemSameComponents(recipe.value().getResultItem(registryAccess), itemStack) && indexAttempt-- == 0) {
                return recipe;
            }
        }
        throw new IllegalArgumentException("Could not find recipe for " + itemStack + "::"
                + itemStack.getComponents() + " with index " + index);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipeCached(RecipeType<T> recipeType,
                                                                                                          C inventoryCrafting,
                                                                                                          Level world, boolean uniqueInventory) {
        return (Optional) CACHE_RECIPES.getUnchecked(Triple.of(recipeType,
                new CacheableCraftingInventory(inventoryCrafting, !uniqueInventory), world.dimension().location()));
    }

    public class CacheableCraftingInventory {

        private final RecipeInput inventoryCrafting;

        public CacheableCraftingInventory(RecipeInput inventoryCrafting, boolean copyInventory) {
            if (copyInventory) {
                // Deep-copy of the inventory to enable caching
                int width = inventoryCrafting.size();
                int height = 1;
                if (inventoryCrafting instanceof CraftingInput) {
                    width = ((CraftingInput) inventoryCrafting).width();
                    height = ((CraftingInput) inventoryCrafting).height();
                }
                int size = inventoryCrafting.size();
                NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
                for (int i = 0; i < inventoryCrafting.size(); i++) {
                    items.set(i, inventoryCrafting.getItem(i).copy());
                }
                this.inventoryCrafting = CraftingInput.of(width, height, items);
            } else {
                this.inventoryCrafting = inventoryCrafting;
            }
        }

        public RecipeInput getInventoryCrafting() {
            return inventoryCrafting;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CacheableCraftingInventory)) {
                return false;
            }
            RecipeInput otherInput = ((CacheableCraftingInventory) obj).getInventoryCrafting();
            if (getInventoryCrafting() instanceof CraftingInput craftingInputThis) {
                if (otherInput instanceof CraftingInput craftingInputOther) {
                    if (craftingInputThis.width() != craftingInputOther.width() || craftingInputThis.height() != craftingInputOther.height()) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (getInventoryCrafting().size() != otherInput.size()) {
                    return false;
                }
            }
            for (int i = 0; i < getInventoryCrafting().size(); i++) {
                if (!ItemStack.isSameItemSameComponents(getInventoryCrafting().getItem(i), otherInput.getItem(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 11 + getInventoryCrafting().size();
            if (getInventoryCrafting() instanceof CraftingInput craftingInput) {
                hash = 23 + 3 * craftingInput.width() + 5 * craftingInput.height();
            }
            for (int i = 0; i < getInventoryCrafting().size(); i++) {
                hash |= modHelpers.getItemStackHelpers().getItemStackHashCode(getInventoryCrafting().getItem(i)) * i;
            }
            return hash;
        }

    }

}
