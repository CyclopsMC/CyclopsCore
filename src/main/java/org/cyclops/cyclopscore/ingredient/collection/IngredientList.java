package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.List;

/**
 * An ingredient collection using list semantics.
 * This means that instances exist in a predefined order and that instances can exist multiple time in the collection.
 *
 * @see List
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public abstract class IngredientList<T, M> extends IngredientCollectionCollectionAdapter<T, M, List<T>> {

    public IngredientList(IngredientComponent<T, M> component, List<T> list) {
        super(component, list);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IngredientList
                && IngredientCollections.equalsCheckedOrdered(this, (IIngredientCollection<T, M>) obj);
    }
}
