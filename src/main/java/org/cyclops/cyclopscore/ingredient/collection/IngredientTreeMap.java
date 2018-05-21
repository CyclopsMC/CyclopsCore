package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.TreeMap;

/**
 * An ingredient map collection that internally uses an {@link TreeMap} to store instances.
 * This means that key instances are automatically ordered based on their component type's comparator.
 * @see TreeMap
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public class IngredientTreeMap<T, M, V> extends IngredientMapWrappedAdapter<T, M, V, TreeMap<IngredientInstanceWrapper<T, M>, V>> {

    public IngredientTreeMap(IngredientComponent<T, M> component) {
        this(component, Maps.newTreeMap());
    }

    public IngredientTreeMap(IngredientComponent<T, M> component, IIngredientMap<? extends T, M, ? extends V> map) {
        this(component);
        putAll(map);
    }

    public IngredientTreeMap(IngredientComponent<T, M> component, TreeMap<IngredientInstanceWrapper<T, M>, V> map) {
        super(component, map);
    }

    @Override
    public IngredientSet<T, M> keySet() {
        return new IngredientTreeSet<>(this.getComponent(), Sets.newTreeSet(getCollection().keySet()));
    }

}
