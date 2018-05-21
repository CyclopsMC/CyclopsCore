package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;

import java.util.HashMap;

/**
 * An ingredient map collection that internally uses an {@link java.util.HashMap} to store instances.
 * This means that key instances are hashed based on their component type's hash method.
 * @see java.util.HashMap
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @param <V> The type of mapped values.
 */
public class IngredientHashMap<T, M, V> extends IngredientMapWrappedAdapter<T, M, V, HashMap<IngredientInstanceWrapper<T, M>, V>> {

    public IngredientHashMap(IngredientComponent<T, M> component) {
        this(component, Maps.newHashMap());
    }

    public IngredientHashMap(IngredientComponent<T, M> component, int expectedSize) {
        this(component, Maps.newHashMapWithExpectedSize(expectedSize));
    }

    public IngredientHashMap(IngredientComponent<T, M> component, IIngredientMap<? extends T, M, ? extends V> map) {
        this(component);
        putAll(map);
    }

    public IngredientHashMap(IngredientComponent<T, M> component, HashMap<IngredientInstanceWrapper<T, M>, V> map) {
        super(component, map);
    }

}
