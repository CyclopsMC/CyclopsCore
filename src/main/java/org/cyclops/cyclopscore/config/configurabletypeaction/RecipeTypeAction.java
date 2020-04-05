package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;

/**
 * The action used for {@link RecipeTypeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeTypeAction<T extends IRecipe<?>> extends ConfigurableTypeAction<RecipeTypeConfig<T>, IRecipeType<T>> {

}
