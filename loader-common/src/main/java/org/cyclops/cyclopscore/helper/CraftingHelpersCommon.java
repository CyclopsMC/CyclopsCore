package org.cyclops.cyclopscore.helper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author rubensworks
 */
public class CraftingHelpersCommon implements ICraftingHelpers {

    private final IModHelpers modHelpers;
    private final LoadingCache<Triple<RecipeType<?>, CacheableCraftingInventory, Identifier>, Optional<RecipeHolder<? extends Recipe>>> CACHE_RECIPES = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Triple<RecipeType<?>, CacheableCraftingInventory, Identifier>, Optional<RecipeHolder<? extends Recipe>>>() {
                @Override
                public Optional<RecipeHolder<? extends Recipe>> load(Triple<RecipeType<?>, CacheableCraftingInventory, Identifier> key) throws Exception {
                    ServerLevel world = modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(ResourceKey.create(Registries.DIMENSION, key.getRight()));
                    return world.recipeAccess().getRecipeFor((RecipeType) key.getLeft(), key.getMiddle().getInventoryCrafting(), world);
                }
            });

    public CraftingHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public RecipeManager getRecipeManager() {
        return Objects.requireNonNull(modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(Level.OVERWORLD), "Server is still loading").recipeAccess();
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> getRecipe(RecipeType<T> recipeType, ResourceKey<Recipe<?>> recipeName) {
        return Optional.ofNullable(getRecipeManager().byKeyTyped(recipeType, recipeName));
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipe(RecipeType<T> recipeType, C container, Level world) {
        return ((RecipeManager) world.recipeAccess()).getRecipeFor(recipeType, container, world);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(RecipeType<? extends T> recipeType) {
        return findRecipes(Objects.requireNonNull(modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(Level.OVERWORLD)), recipeType);
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> findRecipes(ServerLevel world, RecipeType<? extends T> recipeType) {
        return (List<RecipeHolder<T>>) (List) world.recipeAccess().recipes.byType(recipeType);
    }

    @Override
    public List<RecipeDisplayEntry> getRecipeDisplays(RecipeType<?> recipeType, ResourceKey<Recipe<?>> recipeName) {
        List<RecipeDisplayEntry> displays = Lists.newArrayList();
        modHelpers.getMinecraftHelpers().getCurrentServer().overworld().recipeAccess().listDisplaysForRecipe(recipeName, displays::add);
        return displays;
    }

    @Override
    public List<Pair<Identifier, RecipeDisplayEntry>> getRecipeDisplays(RecipeType<?> recipeType, String recipeNameRegex) {
        List<Pair<Identifier, RecipeDisplayEntry>> displays = Lists.newArrayList();
        for (Map.Entry<ResourceKey<Recipe<?>>, List<RecipeManager.ServerDisplayInfo>> entry : modHelpers.getMinecraftHelpers().getCurrentServer().overworld().recipeAccess().recipeToDisplay.entrySet()) {
            if (recipeNameRegex.isEmpty() || entry.getKey().identifier().toString().matches(recipeNameRegex)) {
                for (RecipeManager.ServerDisplayInfo display : entry.getValue()) {
                    displays.add(Pair.of(display.parent().id().identifier(), display.display()));
                }
            }
        }
        return displays;
    }

    @Override
    public <C extends RecipeInput, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipeCached(RecipeType<T> recipeType,
                                                                                                          C inventoryCrafting,
                                                                                                          Level world, boolean uniqueInventory) {
        return (Optional) CACHE_RECIPES.getUnchecked(Triple.of(recipeType,
                new CacheableCraftingInventory(inventoryCrafting, !uniqueInventory), world.dimension().identifier()));
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
