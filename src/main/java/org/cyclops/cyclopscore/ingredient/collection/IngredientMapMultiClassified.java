package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import javax.annotation.Nullable;
import java.util.Collection;
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
 * An ingredient map that classifies instances in smaller collections based on all category type of a component type.
 *
 * This allows instances to be looked up or removed more efficiently when the used match condition
 * is compatible with the identifying match condition of category types.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public class IngredientMapMultiClassified<T, M, V> extends IngredientMapAdapter<T, M, V>
        implements IIngredientMapMutable<T, M, V>, IIngredientCollectionLikeMultiClassifiedTrait<T, M, Map.Entry<T, V>, IngredientMapSingleClassified<T, M, V, ?>> {

    private final Map<IngredientComponentCategoryType<T, M, ?>, IngredientMapSingleClassified<T, M, V, ?>> classifiedMaps;

    public IngredientMapMultiClassified(IngredientComponent<T, M> component,
                                         Supplier<IIngredientMapMutable<T, M, V>> mapCreator) {
        super(component);
        this.classifiedMaps = Maps.newIdentityHashMap();
        for (IngredientComponentCategoryType<T, M, ?> categoryType : component.getCategoryTypes()) {
            this.classifiedMaps.put(categoryType,
                    new IngredientMapSingleClassified<>(component, mapCreator, categoryType));
        }
    }

    @Override
    public void clear() {
        for (IIngredientMapMutable<T, M, V> singleClassified : classifiedMaps.values()) {
            singleClassified.clear();
        }
    }

    @Nullable
    @Override
    public V put(T key, V value) {
        V previous = null;
        for (IIngredientMapMutable<T, M, V> singleClassified : classifiedMaps.values()) {
            previous = singleClassified.put(key, value);
        }
        return previous;
    }

    /**
     * A multi-threaded putAll implementation.
     * This will create workers for adding the given instances to all classified collections.
     * @param executorService An optional executor service.
     *                        If none is provided, a new temporary one will be created
     *                        for the duration of this method call.
     * @param map The map entries that needs to be added.
     * @return The amount of entries that were added.
     */
    public int putAllThreaded(@Nullable ExecutorService executorService, IIngredientMap<? extends T, M, ? extends V> map) {
        boolean newExecutorService = false;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(this.classifiedMaps.size());
            newExecutorService = true;
        }
        try {
            int ret =  executorService.invokeAll(this.classifiedMaps.values().stream()
                    .map(c -> (Callable<Integer>) () -> c.putAll(map)).collect(Collectors.toList())).get(0).get();
            if (newExecutorService) {
                executorService.shutdown();
            }
            return ret;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Nullable
    @Override
    public V remove(T key) {
        V previous = null;
        for (IIngredientMapMutable<T, M, V> singleClassified : classifiedMaps.values()) {
            previous = singleClassified.remove(key);
        }
        return previous;
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
        IngredientMapSingleClassified<T, M, V, ?> classifiedCollection = getBestClassifiedCollection(matchCondition);

        // Determine all other classified collections
        Set<IngredientMapSingleClassified<T, M, V, ?>> otherClassifiedCollections = Sets.newIdentityHashSet();
        otherClassifiedCollections.addAll(classifiedMaps.values());
        otherClassifiedCollections.remove(classifiedCollection);

        // Using the best collection's iterator, we remove all its matches,
        // but we also remove all matches from all other classified collections.
        // This is more efficient than calling removeAll on all classified collections directly,
        // since iteration can (most likely) not be done efficiently for all of them.
        Iterator<Map.Entry<T, V>> it = classifiedCollection.iterator(instance, matchCondition);
        int count = 0;
        while (it.hasNext()) {
            Map.Entry<T, V> removed = it.next();
            it.remove();
            otherClassifiedCollections.forEach((c) -> c.remove(removed.getKey()));
            count++;
        }

        return count;
    }

    @Override
    public boolean containsValue(V value) {
        return getFirstSingleClassified().containsValue(value);
    }

    @Override
    public boolean containsKey(T instance, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).containsKey(instance, matchCondition);
    }

    @Override
    public boolean containsKeyAll(Iterable<? extends T> instances, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).containsKeyAll(instances, matchCondition);
    }

    @Nullable
    @Override
    public V get(T key) {
        return getFirstSingleClassified().get(key);
    }

    @Override
    public Collection<V> getAll(T key, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).getAll(key, matchCondition);
    }

    @Override
    public IngredientSet<T, M> keySet() {
        return getFirstSingleClassified().keySet();
    }

    @Override
    public IngredientSet<T, M> keySet(T instance, M matchCondition) {
        return getBestClassifiedCollection(matchCondition).keySet(instance, matchCondition);
    }

    @Override
    public Collection<V> values() {
        return getFirstSingleClassified().values();
    }

    @Override
    public int size() {
        return getFirstSingleClassified().size();
    }

    @Override
    public Iterator<Map.Entry<T, V>> iterator() {
        return new IIngredientCollectionLikeMultiClassifiedTrait.RemoveCallbackIterator<>(this,
                null, getComponent().getMatcher().getAnyMatchCondition());
    }

    @Override
    public Iterator<Map.Entry<T, V>> iterator(T instance, M matchCondition) {
        return new IIngredientCollectionLikeMultiClassifiedTrait.RemoveCallbackIterator<>(this, instance, matchCondition);
    }

    @Override
    public void removeInstance(IngredientMapSingleClassified<T, M, V, ?> collection, Map.Entry<T, V> iterableInstance) {
        collection.remove(iterableInstance.getKey());
    }

    @Override
    public Map<IngredientComponentCategoryType<T, M, ?>, IngredientMapSingleClassified<T, M, V, ?>> getClassifiedCollections() {
        return this.classifiedMaps;
    }
}
