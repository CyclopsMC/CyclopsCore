package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import java.util.function.Supplier;

/**
 * An ingredient collection that classifies instances in smaller collections based on a category type,
 * but multiple instances that are equal (ignoring quantity) are collapsed into one.
 *
 * @author rubensworks
 */
public class IngredientCollectionSingleClassifiedCollapsed<T, M, C>
        extends IngredientCollectionSingleClassified<T, M, C, IIngredientCollapsedCollectionMutable<T, M>>
        implements IIngredientCollapsedCollectionMutable<T, M> {

    /**
     * Create a new instance.
     * @param component         A component type.
     * @param collectionCreator A callback for creating new internal collections for a single classifier.
     * @param categoryType      A category type using which this collection will classify instance.
     */
    public IngredientCollectionSingleClassifiedCollapsed(
            IngredientComponent<T, M> component,
            Supplier<IIngredientCollapsedCollectionMutable<T, M>> collectionCreator,
            IngredientComponentCategoryType<T, M, C> categoryType) {
        super(component, collectionCreator, categoryType);
    }

    @Override
    public int size() {
        return this.getClassifiedCollections().values().stream().mapToInt(IIngredientCollectionLike::size).sum();
    }

    @Override
    public long getQuantity(T instance) {
        IIngredientCollapsedCollectionMutable<T, M> collection = getClassifiedCollections().get(getClassifier(instance));
        return collection != null ? collection.getQuantity(instance) : 0;
    }

    @Override
    public void setQuantity(T instance, long quantity) {
        getOrCreateClassifiedCollection(getClassifier(instance)).setQuantity(instance, quantity);
    }
}
