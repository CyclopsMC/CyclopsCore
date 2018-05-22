package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    /**
     * A multi-threaded addAll implementation.
     * This will create workers for adding the given instances to all classified collections.
     * @param executorService An optional executor service.
     *                        If none is provided, a new temporary one will be created
     *                        for the duration of this method call.
     * @param instances The instances that need to be added.
     * @return If the collection was changed due to this addition.
     *         This can be false in the case of sets in which each instance can only exists once.
     */
    public boolean addAllThreaded(@Nullable ExecutorService executorService, Iterable<? extends T> instances) {
        boolean newExecutorService = false;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(this.classifiedCollections.size());
            newExecutorService = true;
        }
        try {
            boolean ret =  executorService.invokeAll(this.classifiedCollections.values().stream()
                    .map(c -> (Callable<Boolean>) () -> c.addAll(instances)).collect(Collectors.toList())).get(0).get();
            if (newExecutorService) {
                executorService.shutdown();
            }
            return ret;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
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
