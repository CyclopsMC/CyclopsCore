package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * An {@link Ingredient} with public constructors.
 * This is needed for ingredients that are created before tags are initialized,
 * and for which we need to skip the isEmpty check in fromValues.
 * @author rubensworks
 */
public class IngredientPublicConstructor extends Ingredient {
    public IngredientPublicConstructor(Stream<? extends Value> p_43907_) {
        super(p_43907_);
    }

    public IngredientPublicConstructor(Stream<? extends Value> p_43907_, Supplier<? extends IngredientType<?>> type) {
        super(p_43907_, type);
    }
}
