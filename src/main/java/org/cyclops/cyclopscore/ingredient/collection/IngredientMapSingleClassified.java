package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * An ingredient map that classifies instances in smaller collections based on a category type.
 *
 * This allows instances to be looked up or removed more efficiently when the used match condition
 * is compatible with the identifying match condition of the configured category type.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public class IngredientMapSingleClassified<T, M, V, C> extends IngredientMapAdapter<T, M, V>
        implements IIngredientCollectionLikeSingleClassifiedTrait<T, M, Map.Entry<T, V>, C, IIngredientMapMutable<T, M, V>> {

    private final Map<C, IIngredientMapMutable<T, M, V>> classifiedMaps;
    private final Supplier<IIngredientMapMutable<T, M, V>> mapCreator;
    private final IngredientComponentCategoryType<T, M, C> categoryType;

    private int size;

    /**
     * Create a new instance.
     * @param component A component type.
     * @param mapCreator A callback for creating new internal maps for a single classifier.
     * @param categoryType A category type using which this collection will classify instance.
     */
    public IngredientMapSingleClassified(IngredientComponent<T, M> component,
                                         Supplier<IIngredientMapMutable<T, M, V>> mapCreator,
                                         IngredientComponentCategoryType<T, M, C> categoryType) {
        super(component);
        this.classifiedMaps = categoryType.isReferenceEqual() ? Maps.newIdentityHashMap() : Maps.newHashMap();
        this.mapCreator = mapCreator;
        this.categoryType = categoryType;

        this.size = 0;
    }

    @Override
    public IngredientComponentCategoryType<T, M, C> getCategoryType() {
        return categoryType;
    }

    @Override
    public IIngredientMapMutable<T, M, V> createEmptyCollection() {
        return this.mapCreator.get();
    }

    @Override
    public T getInstance(Map.Entry<T, V> iterableInstance) {
        return iterableInstance.getKey();
    }

    @Override
    public Map<C, IIngredientMapMutable<T, M, V>> getClassifiedCollections() {
        return this.classifiedMaps;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void clear() {
        this.classifiedMaps.clear();
        this.size = 0;
    }

    @Nullable
    @Override
    public V put(T key, V value) {
        V previousValue = getOrCreateClassifiedCollection(getClassifier(key)).put(key, value);
        if (previousValue == null) {
            this.size++;
        }
        return previousValue;
    }

    @Nullable
    @Override
    public V remove(T key) {
        C classifier = getClassifier(key);
        IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(classifier);
        if (map != null) {
            V removed = map.remove(key);
            if (removed != null) {
                this.size--;
                // Cleanup maps map if the map becomes empty
                if (map.isEmpty()) {
                    this.classifiedMaps.remove(classifier);
                }
            }
            return removed;

        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean containsKey(T instance, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(getClassifier(instance));
            if (map != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return true;
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return map.containsKey(instance, subMatchCondition);
                }
            }
        }
        return super.containsKey(instance, matchCondition);
    }

    @Override
    public int countKey(T instance, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(getClassifier(instance));
            if (map != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return map.size();
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return map.countKey(instance, subMatchCondition);
                }
            }
        }
        return super.countKey(instance, matchCondition);
    }

    @Override
    public boolean containsValue(V value) {
        for (IIngredientMapMutable<T, M, V> map : this.classifiedMaps.values()) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public V get(T key) {
        C classifier = getClassifier(key);
        IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(classifier);
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public IngredientSet<T, M> keySet() {
        IngredientHashSet<T, M> keys = new IngredientHashSet<>(this.getComponent());
        for (IIngredientMapMutable<T, M, V> map : this.classifiedMaps.values()) {
            keys.addAll(map.keySet());
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = Lists.newArrayList();
        for (IIngredientMapMutable<T, M, V> map : this.classifiedMaps.values()) {
            values.addAll(map.values());
        }
        return values;
    }

    @Override
    public Collection<V> getAll(T key, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(getClassifier(key));
            if (map != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return map.values();
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return map.getAll(key, subMatchCondition);
                }
            }
        }
        return super.getAll(key, matchCondition);
    }

    @Override
    public IngredientSet<T, M> keySet(T key, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            IIngredientMapMutable<T, M, V> map = this.classifiedMaps.get(getClassifier(key));
            if (map != null) {
                if (Objects.equals(getCategoryType().getMatchCondition(), matchCondition)) {
                    return map.keySet();
                } else {
                    M subMatchCondition = getComponent().getMatcher().withoutCondition(matchCondition, getCategoryType().getMatchCondition());
                    return map.keySet(key, subMatchCondition);
                }
            }
        }
        return super.keySet(key, matchCondition);
    }

    @Override
    public Iterator<Map.Entry<T, V>> iterator() {
        return new IIngredientCollectionLikeSingleClassifiedTrait.ClassifiedIterator<>(this);
    }

    @Override
    public Iterator<Map.Entry<T, V>> iterator(T instance, M matchCondition) {
        if (appliesToClassifier(matchCondition)) {
            // At most one classifier will be found, so we can simply delegate this call
            IIngredientMapMutable<T, M, V> collection = this.getClassifiedCollections().get(getClassifier(instance));
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
