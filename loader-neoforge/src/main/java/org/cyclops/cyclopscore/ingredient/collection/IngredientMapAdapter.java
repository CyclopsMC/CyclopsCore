package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An abstract collection that wraps instances in a {@link IngredientInstanceWrapper}
 * and stores those in a regular {@link Map}.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public abstract class IngredientMapAdapter<T, M, V> implements IIngredientMapMutable<T, M, V> {

    private final IngredientComponent<T, M> component;

    protected IngredientMapAdapter(IngredientComponent<T, M> component) {
        this.component = component;
    }

    @Override
    public IngredientComponent<T, M> getComponent() {
        return this.component;
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        Iterator<Map.Entry<T, V>> it = this.iterator(instance, matchCondition);
        int count = 0;
        while (it.hasNext()) {
            it.next();
            it.remove();
            count++;
        }
        return count;
    }

    @Override
    public Iterator<Map.Entry<T, V>> iterator(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return this.iterator();
        }
        return new FilteredIngredientMapIterator<>(this.iterator(), getComponent().getMatcher(), instance, matchCondition);
    }

    @Override
    public Collection<V> getAll(T key, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return this.values();
        }
        IngredientSet<T, M> keys = this.keySet(key, matchCondition);
        List<V> values = Lists.newArrayListWithCapacity(keys.size());
        for (T instance : keys) {
            values.add(get(instance));
        }
        return values;
    }

    @Override
    public IngredientSet<T, M> keySet(T instance, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            return this.keySet();
        }
        IngredientHashSet<T, M> filteredKeys = new IngredientHashSet<>(getComponent(), this.size());
        Iterator<T> iterator = keySet().iterator(instance, matchCondition);
        while(iterator.hasNext()) {
            filteredKeys.add(iterator.next());
        }
        return filteredKeys;
    }

    @Override
    public Set<Map.Entry<T, V>> entrySet() {
        return Sets.newHashSet(iterator());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IIngredientMap && IngredientCollections.equalsMap(this, (IIngredientMap<?, ?, ?>) obj);
    }

    @Override
    public int hashCode() {
        return IngredientCollections.hash(this);
    }

    @Override
    public String toString() {
        return IngredientCollections.toString(this);
    }
}
