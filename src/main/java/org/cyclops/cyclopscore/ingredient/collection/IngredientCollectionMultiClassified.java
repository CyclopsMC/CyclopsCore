package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * An ingredient collection that classifies instances in smaller collections
 * based on all category type of a component type.
 *
 * This allows instances to be looked up or removed more efficiently when the used match condition
 * is compatible with the identifying match condition of category types.
 *
 * @param <T> An instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionMultiClassified<T, M> extends IngredientCollectionAdapter<T, M>
        implements IIngredientCollectionMutable<T, M>, IIngredientCollectionLikeMultiClassifiedTrait<T, M, T, IngredientCollectionSingleClassified<T, M, ?>> {

    private final Map<IngredientComponentCategoryType<T, M, ?>, IngredientCollectionSingleClassified<T, M, ?>> classifiedCollections;

    public IngredientCollectionMultiClassified(IngredientComponent<T, M> component,
                                               Supplier<IIngredientCollectionMutable<T, M>> collectionCreator) {
        super(component);
        this.classifiedCollections = Maps.newIdentityHashMap();
        for (IngredientComponentCategoryType<T, M, ?> categoryType : component.getCategoryTypes()) {
            this.classifiedCollections.put(categoryType,
                    new IngredientCollectionSingleClassified<>(component, collectionCreator, categoryType));
        }
    }

    @Override
    public boolean add(T instance) {
        boolean result = false;
        for (IngredientCollectionSingleClassified<T, M, ?> singleClassified : classifiedCollections.values()) {
            result = singleClassified.add(instance);
        }
        return result;
    }

    @Override
    public boolean remove(T instance) {
        boolean result = false;
        for (IngredientCollectionSingleClassified<T, M, ?> singleClassified : classifiedCollections.values()) {
            result = singleClassified.remove(instance);
        }
        return result;
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        if (matchCondition.equals(getComponent().getMatcher().getAnyMatchCondition())) {
            int size = this.size();
            this.clear();
            return size;
        }

        // Pick the best classified collection for the given condition,
        // and iterate over all matching elements.
        IngredientCollectionSingleClassified<T, M, ?> classifiedCollection = getBestClassifiedCollection(matchCondition);

        // Determine all other classified collections
        Set<IngredientCollectionSingleClassified<T, M, ?>> otherClassifiedCollections = Sets.newIdentityHashSet();
        otherClassifiedCollections.addAll(classifiedCollections.values());
        otherClassifiedCollections.remove(classifiedCollection);

        // Using the best collection's iterator, we remove all its matches,
        // but we also remove all matches from all other classified collections.
        // This is more efficient than calling removeAll on all classified collections directly,
        // since iteration can (most likely) not be done efficiently for all of them.
        Iterator<T> it = classifiedCollection.iterator(instance, matchCondition);
        int count = 0;
        while (it.hasNext()) {
            T removed = it.next();
            it.remove();
            otherClassifiedCollections.forEach((c) -> c.remove(removed));
            count++;
        }

        return count;
    }

    @Override
    public boolean contains(T instance) {
        return getFirstSingleClassified().contains(instance);
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).contains(instance, matchCondition);
    }

    @Override
    public int count(T instance, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).count(instance, matchCondition);
    }

    @Override
    public void clear() {
        for (IngredientCollectionSingleClassified<T, M, ?> singleClassified : classifiedCollections.values()) {
            singleClassified.clear();
        }
    }

    @Override
    public int size() {
        return getFirstSingleClassified().size();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IngredientCollectionMultiClassified
                && IngredientCollections.equalsCheckedOrdered(this, (IIngredientCollection<T, M>) obj);
    }

    @Override
    public Iterator<T> iterator() {
        return new IIngredientCollectionLikeMultiClassifiedTrait.RemoveCallbackIterator<>(this,
                null, getComponent().getMatcher().getAnyMatchCondition());
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        return new IIngredientCollectionLikeMultiClassifiedTrait.RemoveCallbackIterator<>(this, instance, matchCondition);
    }

    @Override
    public void removeInstance(IngredientCollectionSingleClassified<T, M, ?> collection, T iterableInstance) {
        collection.remove(iterableInstance);
    }

    @Override
    public Map<IngredientComponentCategoryType<T, M, ?>, IngredientCollectionSingleClassified<T, M, ?>> getClassifiedCollections() {
        return this.classifiedCollections;
    }
}
