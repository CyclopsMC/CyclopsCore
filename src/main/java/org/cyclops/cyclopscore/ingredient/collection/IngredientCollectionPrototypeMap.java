package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

/**
 * An ingredient collection that stores the collection as an {@link IIngredientMap}
 * where instances are stored as keys and their amounts as values.
 * By default, a {@link IngredientHashMap} will be used as internal map.
 *
 * Note: This uses slightly different semantics compared to the {@link IIngredientCollection} interface.
 * For instance, multiple instances that are equal (ignoring quantity) are combined by this collection.
 *
 * Optionally, negative quantities can be allowed,
 * which means that even if an instance is not present,
 * it can still be removed, but it will result in a negative quantity.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionPrototypeMap<T, M> extends IngredientCollectionAdapter<T, M> {

    private final IIngredientMapMutable<T, M, Long> ingredients;
    private final boolean negativeQuantities;

    public IngredientCollectionPrototypeMap(IngredientComponent<T, M> component) {
        this(component, false);
    }

    public IngredientCollectionPrototypeMap(IngredientComponent<T, M> component, boolean negativeQuantities) {
        this(component, negativeQuantities, new IngredientHashMap<>(component));
    }

    public IngredientCollectionPrototypeMap(IngredientComponent<T, M> component, boolean negativeQuantities,
                                            IIngredientMapMutable<T, M, Long> map) {
        super(component);
        this.ingredients = map;
        this.negativeQuantities = negativeQuantities;
        if (getComponent().getPrimaryQuantifier() == null) {
            throw new IllegalArgumentException("Quantitative grouping requires a primary quantifier on the component type.");
        }
    }

    @Override
    public boolean add(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T prototype = getPrototype(instance);
        Long value = ingredients.get(prototype);
        long existingValue = value == null ? 0 : value;
        long newValue = Math.addExact(existingValue, matcher.getQuantity(instance));
        if (newValue != 0) {
            ingredients.put(prototype, newValue);
        } else {
            ingredients.remove(prototype);
        }
        return true;
    }

    @Override
    public boolean remove(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        T prototype = getPrototype(instance);
        Long value = ingredients.get(prototype);
        long existingValue = value == null ? 0 : value;
        long currentValue = matcher.getQuantity(instance);
        if (currentValue == existingValue) {
            ingredients.remove(prototype);
            return true;
        } else if (currentValue < existingValue || isNegativeQuantities()) {
            ingredients.put(prototype, existingValue - currentValue);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        ingredients.clear();
    }

    @Override
    public int size() {
        return ingredients.size();
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.transform(ingredients.iterator(), new QuantityApplier<>(getComponent()));
    }

    @Override
    public Iterator<T> iterator(T instance, M matchCondition) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        if (!matcher.hasCondition(matchCondition, getComponent().getPrimaryQuantifier().getMatchCondition())) {
            return Iterators.transform(ingredients.iterator(instance, matchCondition), new QuantityApplier<>(getComponent()));
        }
        return super.iterator(instance, matchCondition);
    }

    /**
     * @return An iterator over all available counted prototypes.
     *         This will contain all non-zero quantity instances, but with a quantity of 1.
     */
    public Iterator<Map.Entry<T, Long>> prototypeIterator() {
        return ingredients.iterator();
    }

    /**
     * @param instance An instance.
     * @param matchCondition A match condition.
     * @return An iterator over all available counted prototypes under the given match conditions.
     *         This will contain all non-zero quantity instances, but with a quantity of 1.
     */
    public Iterator<Map.Entry<T, Long>> prototypeIterator(T instance, M matchCondition) {
        return ingredients.iterator(getPrototype(instance), matchCondition);
    }

    protected T getPrototype(T instance) {
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        return matcher.withQuantity(instance, 1);
    }

    /**
     * Set the quantity of the given instance.
     * @param instance An instance, its quantity will be ignored.
     * @param quantity The new quantity to set.
     */
    public void setQuantity(T instance, long quantity) {
        T prototype = getPrototype(instance);
        if (quantity != 0) {
            ingredients.put(prototype, quantity);
        } else {
            ingredients.remove(prototype);
        }
    }

    /**
     * Get the quantity of the given instance.
     * @param instance An instance, its quantity will be ignored.
     * @return The quantity.
     */
    public long getQuantity(T instance) {
        Long count = ingredients.get(getPrototype(instance));
        return count == null ? 0 : count;
    }

    /**
     * @return If negative quantities are allowed in this collection.
     */
    public boolean isNegativeQuantities() {
        return negativeQuantities;
    }

    protected static class QuantityApplier<T, M> implements Function<Map.Entry<T, Long>, T> {

        private final IIngredientMatcher<T, M> matcher;

        public QuantityApplier(IngredientComponent<T, M> component) {
            this.matcher = component.getMatcher();
        }

        @Nullable
        @Override
        public T apply(@Nullable Map.Entry<T, Long> input) {
            long quantity = Math.min(matcher.getMaximumQuantity(), input.getValue());
            return matcher.withQuantity(input.getKey(), quantity);
        }
    }
}
