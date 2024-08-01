package org.cyclops.commoncapabilities.api.ingredient.storage;

import javax.annotation.Nonnull;

/**
 * A minimal slot-based storage for a type of ingredient component.
 *
 * This is an extension of the slotless {@link IIngredientComponentStorage}.
 *
 * The is interface defines no requirements on how instances must be stored internally.
 * For example, an implementing storage could combine internally several instances together
 * that are equal to each other and take the sum of their quantities.
 *
 * This should not be used as a capability,
 * instead the {@link IIngredientComponentStorageHandler} capability should be used,
 * as this can hold storage for different ingredient components.
 *
 * @see IIngredientComponentStorage
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
 * @author rubensworks
 */
public interface IIngredientComponentStorageSlotted<T, M> extends IIngredientComponentStorage<T, M> {

    /**
     * @return The number of available slots.
     **/
    public int getSlots();

    /**
     * Get the ingredient that is contained in the given slot.
     *
     * Note: The returned ingredient MUST NOT not be modified by the caller.
     *
     * @param slot A slot number.
     * @return The contained ingredient.
     */
    public T getSlotContents(int slot);

    /**
     * Get the maximum allowed quantity in the given slot.
     * @param slot A slot number.
     * @return The maximum allowed in the given slot.
     */
    public long getMaxQuantity(int slot);


    /**
     * Inserts an ingredient into the storage in the given slot and return the remainder.
     * The ingredient should not be modified in this function!
     *
     * @param slot       A slot number to insert to.
     * @param ingredient Ingredient to insert.
     * @param simulate   If true, the insertion is only simulated.
     * @return The remaining ingredient that was not inserted (if the entire ingredient is accepted,
     *         then return {@link org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher#getEmptyInstance()}).
     *         May be the same as the input ingredient if unchanged, otherwise a new ingredient.
     **/
    public T insert(int slot, @Nonnull T ingredient, boolean simulate);

    /**
     * Extract the at most the given quantity from the ingredient from the given slot in the storage.
     *
     * @param slot        A slot number to extract from.
     * @param maxQuantity The maximum amount to extract.
     * @param simulate    If true, the insertion is only simulated
     * @return Ingredient extracted from the slot, must be {@link org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher#getEmptyInstance()},
     *         if nothing can be extracted
     */
    public T extract(int slot, long maxQuantity, boolean simulate);

}
