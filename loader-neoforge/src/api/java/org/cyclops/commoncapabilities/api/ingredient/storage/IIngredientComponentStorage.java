package org.cyclops.commoncapabilities.api.ingredient.storage;

import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * A minimal storage for a type of ingredient component.
 *
 * By not taking into account slots, the storage provider instead of the consumer
 * is responsible for providing an efficient insertion and extraction algorithm.
 *
 * The is interface defines no requirements on how instances must be stored internally.
 * For example, an implementing storage could combine internally several instances together
 * that are equal to each other and take the sum of their quantities.
 *
 * Note: The storage provider MUST ensure deterministic behaviour for extraction and insertion.
 *       For example, the three first returned ingredients hereafter must equal each other.
 *       <pre>
 *          ingredient1 = insert(myIngredient, true);
 *          ingredient2 = insert(myIngredient, true);
 *          ingredient3 = insert(myIngredient, false);
 *          ingredient4 = insert(myIngredient, true); // Can be different again
 *       </pre>
 *       The same applies to extraction.
 *
 * This should not be used as a capability,
 * instead the {@link IIngredientComponentStorageHandler} capability should be used,
 * as this can hold storage for different ingredient components.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
 * @author rubensworks
 */
public interface IIngredientComponentStorage<T, M> extends Iterable<T> {

    /**
     * @return The ingredient component type this storage applies to.
     */
    public IngredientComponent<T, M> getComponent();

    /**
     * Get all ingredients in this storage.
     * @return An iterator over all available ingredients in this storage.
     */
    public Iterator<T> iterator();

    /**
     * Find all ingredients matching the given prototype from the storage.
     *
     * Calling this method will not modify the storage in any way.
     * Results from this method MUST NOT be modified.
     *
     * @param prototype      The ingredient to search for.
     * @param matchCondition The flags to compare the given prototype
     *                       by according to {@link IngredientComponent#getMatcher()}.
     * @return An iterator over ingredients that match the given prototype, which may potentially be empty.
     */
    public Iterator<T> iterator(@Nonnull T prototype, M matchCondition);

    /**
     * @return The maximum allowed quantity over all instances.
     */
    public long getMaxQuantity();

    /**
     * Inserts an ingredient into the storage and return the remainder.
     * The ingredient should not be modified in this function!
     *
     * @param ingredient Ingredient to insert.
     * @param simulate   If true, the insertion is only simulated.
     * @return The remaining ingredient that was not inserted (if the entire ingredient is accepted,
     *         then return {@link org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher#getEmptyInstance()}).
     *         May be the same as the input ingredient if unchanged, otherwise a new ingredient.
     **/
    public T insert(@Nonnull T ingredient, boolean simulate);

    /**
     * Extract an ingredient matching the given prototype from the storage.
     *
     * Note that only the extracted ingredient must match the prototype under the given condition.
     * Internally, ingredients can be combined and matched in any way.
     * For example, an exact match could be produced by combining several ingredients.
     *
     * If the primary quantifier (as identified by {@link IngredientComponent#getPrimaryQuantifier()})
     * IS NOT part of the match condition, then the quantity of the given prototype MUST be interpreted
     * as the maximum quantity that must be extracted.
     *
     * If the primary quantifier (as identified by {@link IngredientComponent#getPrimaryQuantifier()})
     * IS part of the match condition, then the quantity of the given prototype MUST be interpreted
     * as the exact quantity that must be extracted.
     * If the storage has a HIGHER OR EQUAL available quantity,
     * then the storage MUST allow the given quantity to be extracted.
     * If the storage on the other hand has a LOWER available quantity,
     * then extraction is not allowed.
     *
     * @param prototype      The ingredient to search for.
     * @param matchCondition The flags to compare the given prototype
     *                       by according to {@link IngredientComponent#getMatcher()}.
     * @param simulate       If true, the insertion is only simulated
     * @return Ingredient extracted from the slot, must be {@link org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher#getEmptyInstance()},
     *         if nothing can be extracted
     */
    public T extract(@Nonnull T prototype, M matchCondition, boolean simulate);

    /**
     * Extract any ingredient, but the given maximum amount from the storage.
     *
     * @param maxQuantity The maximum amount to extract.
     * @param simulate    If true, the insertion is only simulated
     * @return Ingredient extracted from the slot, must be {@link org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher#getEmptyInstance()},
     *         if nothing can be extracted
     */
    public T extract(long maxQuantity, boolean simulate);

}
