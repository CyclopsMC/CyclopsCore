package org.cyclops.commoncapabilities.api.capability.recipehandler;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectArrayMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import net.minecraft.nbt.Tag;
import org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Collection;

/**
 * A holder for a list of {@link org.cyclops.commoncapabilities.api.ingredient.IPrototypedIngredient} alternatives.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void.
 * @author rubensworks
 */
public interface IPrototypedIngredientAlternatives<T, M> {

    public static Byte2ObjectMap<IPrototypedIngredientAlternatives.ISerializer<?>> SERIALIZERS = new Byte2ObjectArrayMap<>();

    public Collection<IPrototypedIngredient<T, M>> getAlternatives();

    public ISerializer<?> getSerializer();

    public static interface ISerializer<A extends IPrototypedIngredientAlternatives<?, ?>> {

        public byte getId();

        public <T, M> Tag serialize(IngredientComponent<T, M> ingredientComponent, A alternatives);

        public <T, M> A deserialize(IngredientComponent<T, M> ingredientComponent, Tag tag);

    }

}
