package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        implements IIngredientCollectionMutable<T, M> {

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

    @Nullable
    protected IngredientComponentCategoryType<T, M, ?> getFirstMatchingCategory(M matchCondition) {
        // TODO: efficiently cache this if needed
        if (matchCondition.equals(getComponent().getMatcher().getAnyMatchCondition())) {
            return null;
        }
        for (IngredientComponentCategoryType<T, M, ?> categoryType : getComponent().getCategoryTypes()) {
            if (getComponent().getMatcher().hasCondition(matchCondition, categoryType.getMatchCondition())) {
                return categoryType;
            }
        }
        throw new IllegalStateException(
                "Match condition does not match with any of the available category types, this is a coding error.");
    }

    protected IngredientCollectionSingleClassified<T, M, ?> getBestClassifiedCollection(M matchCondition) {
        IngredientComponentCategoryType<T, M, ?> category = getFirstMatchingCategory(matchCondition);
        if (category == null) {
            return getFirstSingleClassified();
        }
        return classifiedCollections.get(category);
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
        List<IngredientCollectionSingleClassified<T, M, ?>> otherClassifiedCollections = Lists.newArrayList(classifiedCollections.values());
        otherClassifiedCollections.remove(classifiedCollection);

        // Using the best collection's iterator, we remove all its matches,
        // but we also remove all matches from all other classified collections.
        // This is more efficient than calling removeAll on all classified collections directly,
        // since iteration can (most likely) not be done efficiently for all of them.
        Iterator<T> it = classifiedCollection.iterator(instance, matchCondition);
        int count = 0;
        while(it.hasNext()) {
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
        if (matchCondition.equals(getComponent().getMatcher().getAnyMatchCondition())) {
            return this.size() > 0;
        }
        return getBestClassifiedCollection(matchCondition).contains(instance, matchCondition);
    }

    @Override
    public int count(T instance, M matchCondition) {
        if (matchCondition.equals(getComponent().getMatcher().getAnyMatchCondition())) {
            return this.size();
        }
        return getBestClassifiedCollection(matchCondition).count(instance, matchCondition);
    }

    @Override
    public void clear() {
        for (IngredientCollectionSingleClassified<T, M, ?> singleClassified : classifiedCollections.values()) {
            singleClassified.clear();
        }
    }

    protected IngredientCollectionSingleClassified<T, M, ?> getFirstSingleClassified() {
        return Iterables.getFirst(classifiedCollections.values(), null);
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
        return new RemoveCallbackIterator<>(this,
                null, getComponent().getMatcher().getAnyMatchCondition());
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        return new RemoveCallbackIterator<>(this, instance, matchCondition);
    }

    public static class RemoveCallbackIterator<T, M> implements Iterator<T> {

        private final IngredientCollectionMultiClassified<T, M> multiClassifiedCollection;
        private final Iterator<T> iterator;
        private final List<IngredientCollectionSingleClassified<T, M, ?>> otherClassifiedCollections;

        private T lastNext;

        public RemoveCallbackIterator(IngredientCollectionMultiClassified<T, M> multiClassifiedCollection,
                                      T instance, M matchCondition) {
            this.multiClassifiedCollection = multiClassifiedCollection;
            IngredientCollectionSingleClassified<T, M, ?> classifiedCollection = this.multiClassifiedCollection.getBestClassifiedCollection(matchCondition);
            this.iterator = classifiedCollection.iterator(instance, matchCondition);
            this.otherClassifiedCollections = Lists
                    .newArrayList(this.multiClassifiedCollection.classifiedCollections.values());
            otherClassifiedCollections.remove(classifiedCollection);
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public T next() {
            return lastNext = this.iterator.next();
        }

        @Override
        public void remove() {
            this.iterator.remove();
            otherClassifiedCollections.forEach((c) -> c.remove(lastNext));
        }
    }
}
