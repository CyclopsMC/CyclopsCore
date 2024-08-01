package org.cyclops.commoncapabilities.api.ingredient;

/**
 * A wrapper around an ingredient component instance that properly implements the
 * {@link #equals(Object)}, {@link #hashCode()} and {@link #compareTo(Object)} methods.
 * @author rubensworks
 */
public class IngredientInstanceWrapper<T, M> implements Comparable<IngredientInstanceWrapper<T, M>> {

    private final IngredientComponent<T, M> component;
    private final T instance;

    public IngredientInstanceWrapper(IngredientComponent<T, M> component, T instance) {
        this.component = component;
        this.instance = instance;
    }

    public IngredientComponent<T, M> getComponent() {
        return component;
    }

    public T getInstance() {
        return instance;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IngredientInstanceWrapper)) {
            return false;
        }
        IngredientInstanceWrapper wrapper = (IngredientInstanceWrapper) obj;
        if (wrapper.getComponent() != this.getComponent()) {
            return false;
        }
        Object thatInstance = wrapper.getInstance();
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        return matcher.isInstance(thatInstance) && matcher.matchesExactly(getInstance(), (T) thatInstance);
    }

    @Override
    public int hashCode() {
        return getComponent().getMatcher().hash(getInstance());
    }

    @Override
    public int compareTo(IngredientInstanceWrapper<T, M> o) {
        return getComponent().getMatcher().compare(getInstance(), o.getInstance());
    }
}
