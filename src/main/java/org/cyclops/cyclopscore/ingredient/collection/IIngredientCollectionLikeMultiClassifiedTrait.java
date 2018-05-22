package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An interface-based trait for collections that have a multi-level classification,
 * which classify instances in smaller collections based on all category type of a component type.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <I> The type that can be iterated over. This is typically just T.
 * @param <L> The collection-like type that is being used to store classified partitions.
 */
public interface IIngredientCollectionLikeMultiClassifiedTrait<T, M, I, L extends IIngredientCollectionLike<T, M, I>> extends IIngredientCollectionLike<T, M, I> {

    /**
     * Get the first category type that applies to the given match condition.
     * @param matchCondition A match condition.
     * @return The first match condition, or null if the match condition applies to all.
     */
    @Nullable
    public default IngredientComponentCategoryType<T, M, ?> getFirstMatchingCategory(M matchCondition) {
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

    /**
     * Get the best matching classified collection-like for the given match condition.
     * @param matchCondition A match condition.
     * @return The best matchin classified collection-like.
     */
    public default L getBestClassifiedCollection(M matchCondition) {
        IngredientComponentCategoryType<T, M, ?> category = getFirstMatchingCategory(matchCondition);
        if (category == null) {
            return getFirstSingleClassified();
        }
        return getClassifiedCollections().get(category);
    }

    /**
     * @return The first available classified collection-like.
     */
    public default L getFirstSingleClassified() {
        return Iterables.getFirst(getClassifiedCollections().values(), null);
    }

    /**
     * Remove the given instance from the given collection-like.
     *
     * This is used internally for iteration purposes.
     *
     * @param collection A collection-like.
     * @param iterableInstance An iterator instance.
     */
    public void removeInstance(L collection, I iterableInstance);

    /**
     * @return A mapping of all internal classified collections.
     */
    public Map<IngredientComponentCategoryType<T, M, ?>, L> getClassifiedCollections();

    public static class RemoveCallbackIterator<T, M, I, L extends IIngredientCollectionLike<T, M, I>> implements Iterator<I> {

        private final IIngredientCollectionLikeMultiClassifiedTrait<T, M, I, L> multiClassifiedCollection;
        private final Iterator<I> iterator;
        private final List<L> otherClassifiedCollections;

        private I lastNext;

        public RemoveCallbackIterator(IIngredientCollectionLikeMultiClassifiedTrait<T, M, I, L> multiClassifiedCollection,
                                      T instance, M matchCondition) {
            this.multiClassifiedCollection = multiClassifiedCollection;
            L classifiedCollection = this.multiClassifiedCollection.getBestClassifiedCollection(matchCondition);
            this.iterator = classifiedCollection.iterator(instance, matchCondition);
            this.otherClassifiedCollections = Lists
                    .newArrayList(this.multiClassifiedCollection.getClassifiedCollections().values());
            otherClassifiedCollections.remove(classifiedCollection);
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public I next() {
            return lastNext = this.iterator.next();
        }

        @Override
        public void remove() {
            this.iterator.remove();
            otherClassifiedCollections.forEach((c) -> this.multiClassifiedCollection.removeInstance(c, lastNext));
        }
    }

}
