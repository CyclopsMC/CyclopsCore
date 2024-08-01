package org.cyclops.commoncapabilities.api.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ItemMatch;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.MixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.PrototypedIngredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * A default recipe handler that contains a dirt to diamonds recipe.
 * @author rubensworks
 */
public class DefaultRecipeHandler implements IRecipeHandler {
    @Override
    public Set<IngredientComponent<?, ?>> getRecipeInputComponents() {
        return Sets.newHashSet(IngredientComponent.ITEMSTACK);
    }

    @Override
    public Set<IngredientComponent<?, ?>> getRecipeOutputComponents() {
        return Sets.newHashSet(IngredientComponent.ITEMSTACK);
    }

    @Override
    public boolean isValidSizeInput(IngredientComponent component, int size) {
        return component == IngredientComponent.ITEMSTACK && size == 1;
    }

    @Override
    public List<IRecipeDefinition> getRecipes() {
        return Lists.newArrayList(
                RecipeDefinition.ofIngredient(IngredientComponent.ITEMSTACK,
                        Lists.newArrayList(new PrototypedIngredient<>(IngredientComponent.ITEMSTACK, new ItemStack(Blocks.DIRT), ItemMatch.EXACT)),
                        MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, new ItemStack(Items.DIAMOND))
                )
        );
    }

    @Nullable
    @Override
    public IMixedIngredients simulate(IMixedIngredients input) {
        List<ItemStack> ingredients = input.getInstances(IngredientComponent.ITEMSTACK);
        if (input.getComponents().size() == 1 && ingredients.size() == 1) {
            if (IngredientComponent.ITEMSTACK.getMatcher().matchesExactly(ingredients.get(0), new ItemStack(Blocks.DIRT))) {
                return MixedIngredients.ofInstance(IngredientComponent.ITEMSTACK, new ItemStack(Items.DIAMOND));
            }
        }
        return null;
    }
}
