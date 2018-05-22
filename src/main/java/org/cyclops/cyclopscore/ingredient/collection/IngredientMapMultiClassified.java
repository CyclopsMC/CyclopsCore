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
import java.util.function.Supplier;

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
