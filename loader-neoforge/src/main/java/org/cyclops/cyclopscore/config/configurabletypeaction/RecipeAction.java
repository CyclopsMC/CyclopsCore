package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConfig;

/**
 * The action used for {@link RecipeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeAction<T extends Recipe<?>> extends ConfigurableTypeActionForge<RecipeConfig<T>, RecipeSerializer<T>> {

}
