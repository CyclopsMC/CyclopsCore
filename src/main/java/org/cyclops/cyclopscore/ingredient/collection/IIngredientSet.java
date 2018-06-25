package org.cyclops.cyclopscore.ingredient.collection;

import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * A ingredient collection using set semantics.
 * This means that each instances can only be present once in the collection based on its equals method.
 *
 * @see Set
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientSet<T, M> extends IIngredientCollection<T, M> {

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(this.iterator(), this.size(), Spliterator.DISTINCT);
    }

}
