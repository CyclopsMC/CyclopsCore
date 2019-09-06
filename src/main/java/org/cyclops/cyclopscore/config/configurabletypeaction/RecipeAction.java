package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;

/**
 * The action used for {@link RecipeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeAction<T extends IRecipe<?>> extends ConfigurableTypeAction<RecipeConfig<T>, IRecipeSerializer<T>> {

    @Override
    public void onRegisterForge(RecipeConfig<T> eConfig) {
        register(eConfig.getInstance(), (RecipeConfig) eConfig);
    }
}
