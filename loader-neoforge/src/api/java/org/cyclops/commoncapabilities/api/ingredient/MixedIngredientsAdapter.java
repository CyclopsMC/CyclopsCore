package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract implementation of mixed ingredients.
 * @author rubensworks
 */
public abstract class MixedIngredientsAdapter implements IMixedIngredients {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IMixedIngredients) {
            IMixedIngredients that = (IMixedIngredients) obj;
            if (Sets.newHashSet(this.getComponents()).equals(Sets.newHashSet(that.getComponents()))) {
                for (IngredientComponent<?, ?> component : getComponents()) {
                    List<?> thisInstances = this.getInstances(component);
                    List<?> thatInstances = that.getInstances(component);
                    IIngredientMatcher matcher = component.getMatcher();
                    if (thisInstances.size() == thatInstances.size()) {
                        for (int i = 0; i < thisInstances.size(); i++) {
                            if (!matcher.matchesExactly(thisInstances.get(i), thatInstances.get(i))) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 902;
        for (IngredientComponent<?, ?> component : getComponents()) {
            hash |= component.hashCode() << 2;
            IIngredientMatcher matcher = component.getMatcher();
            for (Object instance : getInstances(component)) {
                hash |= matcher.hash(instance);
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IngredientComponent component : getComponents()) {
            sb.append('{');
            sb.append(component);
            sb.append(':');
            sb.append(getInstances(component).stream().map(instance -> component.getMatcher().toString(instance)).collect(Collectors.toList()));
            sb.append('}');
        }
        return "[MixedIngredients ingredients: " + sb + "]";
    }

    @Override
    public int compareTo(IMixedIngredients that) {
        // Compare input components
        int compComp = MixedIngredientsAdapter.compareCollection(this.getComponents(), that.getComponents());
        if (compComp != 0) {
            return compComp;
        }

        // Compare instances
        for (IngredientComponent component : getComponents()) {
            int compInstance = MixedIngredientsAdapter.compareCollection(
                    this.getInstances(component), that.getInstances(component), component.getMatcher());
            if (compInstance != 0) {
                return compInstance;
            }
        }

        return 0;
    }

    /**
     * Compare two collections with comparable elements.
     * @param a A first collection.
     * @param b A second collection.
     * @param <T> The type of the elements.
     * @return The comparator value.
     */
    public static <T extends Comparable<T>> int compareCollection(Collection<? super T> a, Collection<? super T> b) {
        if (a.size() != b.size()) {
            return a.size() - b.size();
        }

        Object[] aArray = a.toArray();
        Object[] bArray = b.toArray();
        for (int i = 0; i < aArray.length; i++) {
            int compComp = ((T) aArray[i]).compareTo((T) bArray[i]);
            if (compComp != 0) {
                return compComp;
            }
        }
        return 0;
    }

    /**
     * Compare two collections with a custom comparator.
     * @param a A first collection.
     * @param b A second collection.
     * @param comparator The element comparator.
     * @param <T> The type of the elements.
     * @return The comparator value.
     */
    public static <T> int compareCollection(Collection<? super T> a, Collection<? super T> b,
                                            Comparator<T> comparator) {
        if (a.size() != b.size()) {
            return a.size() - b.size();
        }

        Object[] aArray = a.toArray();
        Object[] bArray = b.toArray();
        for (int i = 0; i < aArray.length; i++) {
            int compComp = comparator.compare((T) aArray[i], (T) bArray[i]);
            if (compComp != 0) {
                return compComp;
            }
        }
        return 0;
    }
}
