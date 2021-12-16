package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeTypeConfig;

/**
 * The action used for {@link RecipeTypeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeTypeAction<T extends Recipe<?>> extends ConfigurableTypeAction<RecipeTypeConfig<T>, RecipeType<T>> {

}
