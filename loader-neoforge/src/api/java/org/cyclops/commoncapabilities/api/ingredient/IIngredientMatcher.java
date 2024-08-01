package org.cyclops.commoncapabilities.api.ingredient;

import net.minecraft.network.chat.MutableComponent;

import java.util.Comparator;

/**
 * An instance matcher for certain instance and condition types.
 * @param <T> The instance type to match.
 * @param <M> The matching condition parameter.
 * @author rubensworks
 */
public interface IIngredientMatcher<T, M> extends Comparator<T> {

    /**
     * If the given object is an instance of the ingredient type.
     * @param object An object.
     * @return If the object is a valid instance of the ingredient component type.
     */
    public boolean isInstance(Object object);

    /**
     * @return The match condition that is used to match any instance based on a prototype.
     */
    public M getAnyMatchCondition();

    /**
     * @return The match condition that is used to match instances exactly based on a prototype.
     */
    public M getExactMatchCondition();

    /**
     * @return The match condition that is used to match instances exactly based on a prototype,
     *         excluding the quantity.
     */
    public M getExactMatchNoQuantityCondition();

    /**
     * Create a new match condition based on the given condition that includes the second condition.
     * @param matchCondition The match condition to start from.
     * @param with The match condition to include.
     * @return A new match condition that is the combination of both.
     */
    public M withCondition(M matchCondition, M with);

    /**
     * Create a new match condition based on the given condition that excludes the second condition.
     * @param matchCondition The match condition to start from.
     * @param without A match condition to exclude.
     * @return A new match condition that is the first one without the second one.
     */
    public M withoutCondition(M matchCondition, M without);

    /**
     * Check if the given match condition contains at least the given condition.
     * @param matchCondition The match condition to start from.
     * @param searchCondition A match condition to search for.
     * @return If the first condition contains at least the second condition.
     */
    public boolean hasCondition(M matchCondition, M searchCondition);

    /**
     * Check if the two given instances match based on the given match conditions.
     * @param a A first instance.
     * @param b A second instance.
     * @param matchCondition A condition under which the matching should be done.
     * @return If the two given instances match under the given conditions.
     */
    public boolean matches(T a, T b, M matchCondition);

    /**
     * Check if the two given instances are equal.
     * @param a A first instance.
     * @param b A second instance.
     * @return If the two given instances are equal.
     */
    public default boolean matchesExactly(T a, T b) {
        return matches(a, b, getExactMatchCondition());
    }

    /**
     * @return The instance that acts as an 'empty' instance.
     *         For ItemStacks, this would be ItemStack.EMPTY.
     */
    public T getEmptyInstance();

    /**
     * Check if the given entity matches the empty instance,
     * as provided by {@link #getEmptyInstance()}.
     * @param instance An instance.
     * @return If the instance is empty.
     */
    public default boolean isEmpty(T instance) {
        return instance == getEmptyInstance();
    }

    /**
     * Hash the given instance.
     * This must be calculated quickly.
     * @param instance An instance.
     * @return A hashcode for the given instance.
     */
    public int hash(T instance);

    /**
     * Create a deep copy of the given instance.
     * @param instance An instance.
     * @return A copy of the given instance.
     */
    public T copy(T instance);

    /**
     * Get the quantity in the given instance.
     * @param instance An instance.
     * @return The instance quantity.
     */
    public long getQuantity(T instance);

    /**
     * Create a copy of the given instance with the given quantity.
     * @param instance An instance.
     * @param quantity The new instance quantity.
     * @return The copied instance with the new quantity.
     * @throws ArithmeticException If the given quantity does not fit into the instance anymore.
     */
    public T withQuantity(T instance, long quantity) throws ArithmeticException;

    /**
     * @return The maximum allowed quantity for instances.
     */
    public long getMaximumQuantity();

    /**
     * Compare two conditions with each other.
     * @param a A first condition.
     * @param b A second condition.
     * @return The comparison result.
     */
    public int conditionCompare(M a, M b);

    /**
     * Return the localized name of the given instance.
     * This should not include the instance quantity.
     *
     * Should only be called client-side!
     *
     * @param instance An instance.
     * @return The localized name of the given instance.
     */
    public String localize(T instance);

    /**
     * Return the display name of the given instance.
     * This should not include the instance quantity.
     * @param instance An instance.
     * @return The display name of the given instance.
     */
    public MutableComponent getDisplayName(T instance);

    /**
     * Return a stringified version of the given instance.
     * These strings are mainly used for development purposes.
     * @param instance An instance.
     * @return The string version of the given instance.
     */
    public String toString(T instance);

}
