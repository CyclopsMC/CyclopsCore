package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An ingredient collection that classifies instances in smaller collections based on a category type.
 *
 * This allows instances to be looked up or removed more efficiently when the used match condition
 * is compatible with the identifying match condition of the configured category type.
 *
 * @param <T> An instance type.
 * @param <M> The matching condition parameter.
 * @param <C> A classifier type.
 */
public class IngredientCollectionSingleClassified<T, M, C> extends IngredientCollectionAdapter<T, M>
        implements IIngredientCollectionLikeSingleClassifiedTrait<T, M, T, C, IIngredientCollectionMutable<T, M>> {

    private final Map<C, IIngredientCollectionMutable<T, M>> classifiedCollections;
    private final Supplier<IIngredientCollectionMutable<T, M>> collectionCreator;
    private final IngredientComponentCategoryType<T, M, C> categoryType;

    private int size;

    /**
     * Create a new instance.
     * @param component A component type.
     * @param collectionCreator A callback for creating new internal collections for a single classifier.
     * @param categoryType A category type using which this collection will classify instance.
     */
    public IngredientCollectionSingleClassified(IngredientComponent<T, M> component,
                                                Supplier<IIngredientCollectionMutable<T, M>> collectionCreator,
                                                IngredientComponentCategoryType<T, M, C> categoryType) {
        super(component);
        this.classifiedCollections = categoryType.isReferenceEqual() ? Maps.newIdentityHashMap() : Maps.newHashMap();
        this.collectionCreator = collectionCreator;
        this.categoryType = categoryType;

        this.size = 0;
    }

    /**
     * @return The category type using which this collection classifies.
     */
    public IngredientComponentCategoryType<T, M, C> getCategoryType() {
        return categoryType;
    }

    @Override
    public IIngredientCollectionMutable<T, M> createEmptyCollection() {
        return this.collectionCreator.get();
    }

    @Override
    public T getInstance(T iterableInstance) {
        return iterableInstance;
    }

    @Override
    public Map<C, IIngredientCollectionMutable<T, M>> getClassifiedCollections() {
        return this.classifiedCollections;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean add(T instance) {
        if (getOrCreateClassifiedCollection(getClassifier(instance)).add(instance)) {
            this.size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(T instance) {
        C classifier = getClassifier(instance);
        IIngredientCollectionMutable<T, M> collection = this.classifiedCollections.get(classifier);
        if (collection != null && collection.remove(instance)) {
            this.size--;
            // Cleanup collections map if the collection becomes empty
            if (collection.isEmpty()) {
                this.classifiedCollections.remove(classifier);
            }
            return true;
        }
        return false;
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            int size = size();
            this.clear();
            return size;
        }
        if (appliesToClassifier(matchCondition)) {
            int removed = 0;
            C classifier = getClassifier(instance);
            IIngredientCollectionMutable<T, M> collection = this.classifiedCollections.get(classifier);
            if (collection != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    // If the match condition directly corresponds to the match condition of the category type,
                    // we can directly remove the sub-collection
                    removed = collection.size();
                    collection.clear();
                } else {
                    // Otherwise, remove the match condition of this category type from the query's match condition
                    // so that the sub collection requires less operations.
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    removed = collection.removeAll(instance, subMatchCondition);
                }
                // Cleanup collections map if the collection becomes empty
                if (collection.isEmpty()) {
                    this.classifiedCollections.remove(classifier);
                }
            }
            this.size -= removed;
            return removed;
        }
        return super.removeAll(instance, matchCondition);
    }

    @Override
    public boolean contains(T instance) {
        IIngredientCollectionMutable<T, M> collection = this.classifiedCollections.get(getClassifier(instance));
        return collection != null && collection.contains(instance);
    }

    @Override
    public boolean contains(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return !isEmpty();
        }
        if (appliesToClassifier(matchCondition)) {
            IIngredientCollectionMutable<T, M> collection = this.classifiedCollections.get(getClassifier(instance));
            if (collection != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return true;
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return collection.contains(instance, subMatchCondition);
                }
            }
        }
        return super.contains(instance, matchCondition);
    }

    @Override
    public void clear() {
        this.classifiedCollections.clear();
        this.size = 0;
    }

    @Override
    public int count(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return size();
        }
        if (appliesToClassifier(matchCondition)) {
            IIngredientCollectionMutable<T, M> collection = this.classifiedCollections.get(getClassifier(instance));
            if (collection != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return collection.size();
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return collection.count(instance, subMatchCondition);
                }
            }
        }
        return super.count(instance, matchCondition);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof IngredientCollectionSingleClassified
                && this.getCategoryType() == ((IngredientCollectionSingleClassified) obj).getCategoryType()
                && IngredientCollections.equalsCheckedOrdered(this, (IIngredientCollection<T, M>) obj);
    }

    @Override
    public Iterator<T> iterator() {
        return new IIngredientCollectionLikeSingleClassifiedTrait.ClassifiedIterator<>(this);
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            // At most one classifier will be found, so we can simply delegate this call
            IIngredientCollectionMutable<T, M> collection = this.getClassifiedCollections().get(getClassifier(instance));
            if (collection != null) {
                return new IIngredientCollectionLikeSingleClassifiedTrait.ClassifiedIteratorDelegated<>(
                        this, collection, instance, matchCondition);
            } else {
                return Iterators.forArray();
            }
        }
        return new FilteredIngredientCollectionLikeSingleClassifiedIterator<>(this, getComponent().getMatcher(), instance, matchCondition);
    }

}
