package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponentCategoryType;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A mutable ingredient collection that wraps over another mutable ingredient collection
 * and groups instances by stacking them using their primary quantifier.
 *
 * Warning: The given collection must allow the storage of multiple equal instances such as an {@link IIngredientList}.
 *
 * Warning: If the given collection is mutated outside of this grouper,
 * behaviour becomes undefined.
 *
 * @param <T> An instance type.
 * @param <M> The matching condition parameter.
 * @param <I> The inner collection type.
 */
public class IngredientCollectionQuantitativeGrouper<T, M, I extends IIngredientList<T, M> & IIngredientCollectionMutable<T, M>>
        extends IngredientCollectionMutableWrapper<T, M, I> implements IIngredientCollapsedCollection<T, M> {

    private final IngredientComponentCategoryType<T, M, ?> primaryQuantifier;
    private final M quantifierlessMatchCondition;
    private final long maxQuantity;

    public IngredientCollectionQuantitativeGrouper(I innerCollection,
                                                   boolean ignoreInnerCollectionEmptyCheck) {
        super(innerCollection);

        // Safety check to see if the given collection is empty.
        if (!ignoreInnerCollectionEmptyCheck && !innerCollection.isEmpty()) {
            throw new IllegalArgumentException("Inner collections must be empty before grouping.");
        }

        this.primaryQuantifier = getComponent().getPrimaryQuantifier();
        if (this.primaryQuantifier == null) {
            throw new IllegalArgumentException("Quantitative grouping requires a primary quantifier on the component type.");
        }
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        this.quantifierlessMatchCondition = matcher.withoutCondition(matcher.getExactMatchCondition(),
                getPrimaryQuantifier().getMatchCondition());
        this.maxQuantity = matcher.getMaximumQuantity();
    }

    public IngredientCollectionQuantitativeGrouper(I innerCollection) {
        this(innerCollection, false);
    }

    protected IngredientComponentCategoryType<T, M, ?> getPrimaryQuantifier() {
        return primaryQuantifier;
    }

    protected M getQuantifierlessMatchCondition() {
        return quantifierlessMatchCondition;
    }

    @Override
    public boolean contains(T instance) {
        Iterator<T> it = iterator(instance, getQuantifierlessMatchCondition());
        if (!it.hasNext()) {
            // Fail if there are no matches
            return false;
        }
        T first = it.next();
        if (it.hasNext()) {
            // If we have more than one matches, at least one of them has the max quantity
            return true;
        }
        // Otherwise, check if the single existing instance has a quantity
        // that is equal or higher than the given instance quantity.
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        return matcher.getQuantity(instance) <= matcher.getQuantity(first);
    }

    @Override
    public boolean containsAll(Iterable<? extends T> instances) {
        for (T instance : instances) {
            if (!contains(instance)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean add(T instance) {
        // If an equal instance already exists, add its quantity
        Iterator<T> it = iterator(instance, getQuantifierlessMatchCondition());
        while (it.hasNext()) {
            IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
            T existing = it.next();

            // Check if the combined amount will be within the accepted amount.
            long remainingAllowedQuantity = maxQuantity - matcher.getQuantity(existing);
            long newQuantity = matcher.getQuantity(instance);
            if (remainingAllowedQuantity == 0) {
                // This instance reached its max quantity
                // continue;
            } else if (remainingAllowedQuantity >= newQuantity) {
                // If so, combine both instance quantities

                // Remove the existing instance, as we consider instances as being immutable
                it.remove();

                long combinedQuantity = Math.addExact(matcher.getQuantity(existing), matcher.getQuantity(instance));
                T joinedInstance = matcher.withQuantity(instance, combinedQuantity);
                return super.add(joinedInstance);
            } else {
                // If not, split up into two separate instances where we fill the first one to the maximum amount.

                // Remove the existing instance, as we consider instances as being immutable
                it.remove();

                long remainingQuantity = newQuantity - remainingAllowedQuantity;
                return super.add(matcher.withQuantity(existing, maxQuantity))
                        & super.add(matcher.withQuantity(instance, remainingQuantity));
            }
        }
        return super.add(instance);
    }

    @Override
    public boolean addAll(Iterable<? extends T> instances) {
        boolean changed = false;
        for (T instance : instances) {
            changed |= add(instance);
        }
        return changed;
    }

    @Override
    public boolean remove(T instance) {
        // If the instance already exists,
        // and its quantity is equal or higher than the given instance quantity,
        // subtract its quantity
        List<T> existingInstances = Lists.newArrayList(iterator(instance, getQuantifierlessMatchCondition()));
        if (existingInstances.isEmpty()) {
            return false;
        }

        // Sort matching instances by increasing quantities
        IIngredientMatcher<T, M> matcher = getComponent().getMatcher();
        existingInstances.sort((o1, o2) -> (int) (matcher.getQuantity(o1) - matcher.getQuantity(o2)));
        boolean hasMaxInstance = existingInstances.size() > 1; // If at least one instance with max quantity exists

        // Iterate through existing instances and remove until the desired amount is reached
        long toSubtract = matcher.getQuantity(instance);
        Iterator<T> it = existingInstances.iterator();
        while (it.hasNext()) {
            T existing = it.next();

            long existingQuantity = matcher.getQuantity(existing);
            if (existingQuantity > toSubtract) {
                // Remove the existing instance, as we consider instances as being immutable
                super.remove(existing);
                // Add a new instance with the reduced amount
                super.add(matcher.withQuantity(instance, existingQuantity - toSubtract));
                return true;
            } else if (existingQuantity == toSubtract) {
                // Remove the existing instance, as we consider instances as being immutable
                super.remove(existing);
                // Don't remove a new instance, as everything was removed
                return true;
            } else if (!hasMaxInstance) {
                // Fail if the given instance quantity was larger than the existing instance quantity,
                // and we have no max instance.
                return false;
            } else {
                // Remove the existing instance, as we consider instances as being immutable
                super.remove(existing);
                // Reduce what we have to subtract with as much as we can with the current existing instance.
                // The next iteration is guaranteed to be able to handle the remaining quantity.
                toSubtract -= existingQuantity;
            }
        }
        throw new IllegalStateException("Failed to remove due to invalid instance grouping: " + existingInstances);
    }

    @Override
    public int removeAll(T instance, M matchCondition) {
        Iterator<T> it = this.iterator(instance, matchCondition);
        int count = 0;
        while (it.hasNext()) {
            it.next();
            it.remove();
            count++;
        }
        return count;
    }

    @Override
    public int removeAll(Iterable<? extends T> instances) {
        int removed = 0;
        for (T instance : instances) {
            if (remove(instance)) {
                removed++;
            }
        }
        return removed;
    }

    @Override
    public int removeAll(Iterable<? extends T> instances, M matchCondition) {
        if (Objects.equals(getComponent().getMatcher().getAnyMatchCondition(), matchCondition)) {
            int size = size();
            this.clear();
            return size;
        }
        int removed = 0;
        for (T instance : instances) {
            removed += removeAll(instance, matchCondition);
        }
        return removed;
    }
}
