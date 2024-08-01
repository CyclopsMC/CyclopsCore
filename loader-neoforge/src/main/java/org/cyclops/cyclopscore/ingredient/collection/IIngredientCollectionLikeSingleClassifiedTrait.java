package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An interface-based trait for collections that have a single-level classification,
 * which classify instances in smaller collections based on a category type.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <I> The type that can be iterated over. This is typically just T.
 * @param <C> A classifier type.
 * @param <L> The collection-like type that is being used to store classified partitions.
 */
public interface IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L extends IIngredientCollectionLike<T, M, I>> extends IIngredientCollectionLike<T, M, I> {

    /**
     * @return The category type using which this collection classifies.
     */
    public IngredientComponentCategoryType<T, M, C> getCategoryType();

    /**
     * Check if this collection is able to classify the given match condition.
     * @param matchCondition A match condition.
     * @return If the match condition can be classified.
     */
    public default boolean appliesToClassifier(M matchCondition) {
        return getComponent().getMatcher().hasCondition(matchCondition, getCategoryType().getMatchCondition());
    }

    /**
     * Get the classifier for the given instance.
     * @param instance An instance.
     * @return The instance classifier.
     */
    public default C getClassifier(T instance) {
        return getCategoryType().getClassifier().apply(instance);
    }

    /**
     * @return A new collection-like for internal classification usage.
     */
    public L createEmptyCollection();

    /**
     * Get the existing collection-like for the given classifier,
     * or create a new one if it does not exist yet.
     * @param classifier A classifier.
     * @return An existing or new collection-like.
     */
    public default L getOrCreateClassifiedCollection(C classifier) {
        return this.getClassifiedCollections().computeIfAbsent(classifier, k -> createEmptyCollection());
    }

    /**
     * Get the instance from the given iterator instance.
     *
     * This is used internally for iteration purposes.
     * This typically is just a no-op.
     *
     * @param iterableInstance An iterator instance.
     * @return An instance.
     */
    public T getInstance(I iterableInstance);

    /**
     * @return A mapping of all internal classified collections.
     */
    public Map<C, L> getClassifiedCollections();

    /**
     * Set the internal size value.
     *
     * This should not be called outside of internal usage,
     * such as custom iterators that can mutate this collection.
     *
     * @param size The new size.
     */
    public void setSize(int size);

    /**
     * An iterator over a single-level classification that allows removals.
     *
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <I> The type that can be iterated over. This is typically just T.
     * @param <C> A classifier type.
     * @param <L> The collection-like type that is being used to store classified partitions.
     */
    public static class ClassifiedIterator<T, M, I, C, L extends IIngredientCollectionLike<T, M, I>> implements Iterator<I> {

        private final IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection;
        private Iterator<Map.Entry<C, L>> classifierIterator;
        private Iterator<I> instanceIterator;
        private Map.Entry<C, L> lastClassifierEntry;

        public ClassifiedIterator(IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection) {
            this.classifiedCollection = classifiedCollection;
            this.classifierIterator = this.classifiedCollection.getClassifiedCollections().entrySet().iterator();
            this.instanceIterator = null;
        }

        protected void prepareNextIterators() {
            if ((this.instanceIterator == null || !this.instanceIterator.hasNext())
                    && this.classifierIterator.hasNext()) {
                this.lastClassifierEntry = this.classifierIterator.next();
                this.instanceIterator = this.classifiedCollection.getClassifiedCollections()
                        .get(lastClassifierEntry.getKey()).iterator();
            }
        }

        @Override
        public boolean hasNext() {
            prepareNextIterators();
            return this.instanceIterator != null && this.instanceIterator.hasNext();
        }

        @Override
        public I next() {
            prepareNextIterators();
            if (this.instanceIterator == null) {
                throw new NoSuchElementException("No next instances are available");
            }
            return this.instanceIterator.next();
        }

        @Override
        public void remove() {
            if (this.instanceIterator == null) {
                throw new IllegalStateException("The next method was not called yet");
            }
            this.instanceIterator.remove();
            classifiedCollection.setSize(classifiedCollection.size() - 1);
            // Cleanup collections map if the collection becomes empty
            if (this.classifiedCollection.getClassifiedCollections().get(this.lastClassifierEntry.getKey()).isEmpty()) {
                this.classifierIterator.remove();
            }
        }
    }

    /**
     * An iterator over classified collection of a single-level classification that allows removals.
     *
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <I> The type that can be iterated over. This is typically just T.
     * @param <C> A classifier type.
     * @param <L> The collection-like type that is being used to store classified partitions.
     */
    public static class ClassifiedIteratorDelegated<T, M, C, I, L extends IIngredientCollectionLike<T, M, I>> implements Iterator<I> {

        private final IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection;
        private final Iterator<I> iterator;
        private I lastInstance;

        public ClassifiedIteratorDelegated(IIngredientCollectionLikeSingleClassifiedTrait<T, M, I, C, L> classifiedCollection,
                                           L collection, T instance, M matchCondition) {
            if (Objects.equals(classifiedCollection.getCategoryType().getMatchCondition(), matchCondition)) {
                this.iterator = collection.iterator();
            } else {
                M subMatchCondition = classifiedCollection.getComponent().getMatcher().withoutCondition(matchCondition, classifiedCollection.getCategoryType().getMatchCondition());
                this.iterator = collection.iterator(instance, subMatchCondition);
            }
            this.classifiedCollection = classifiedCollection;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public I next() {
            return lastInstance = iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
            classifiedCollection.setSize(classifiedCollection.size() - 1);
            // Cleanup collections map if the collection becomes empty
            C classifier = this.classifiedCollection.getClassifier(classifiedCollection.getInstance(lastInstance));
            if (this.classifiedCollection.getClassifiedCollections().get(classifier).isEmpty()) {
                this.classifiedCollection.getClassifiedCollections().remove(classifier);
            }
        }
    }

}
