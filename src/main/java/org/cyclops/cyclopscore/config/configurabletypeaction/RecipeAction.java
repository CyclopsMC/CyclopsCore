package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;

/**
 * The action used for {@link RecipeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeAction<T extends IRecipe<?>> extends ConfigurableTypeActionForge<RecipeConfig<T>, IRecipeSerializer<T>> {

}
