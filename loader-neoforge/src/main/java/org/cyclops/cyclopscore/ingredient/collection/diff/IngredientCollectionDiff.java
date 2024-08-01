package org.cyclops.cyclopscore.ingredient.collection.diff;

import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollection;

import javax.annotation.Nullable;

/**
 * Data class for a collection of additions and deletions.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public class IngredientCollectionDiff<T, M> {

    @Nullable
    private final IIngredientCollection<T, M> additions;
    @Nullable
    private final IIngredientCollection<T, M> deletions;
    private final boolean completelyEmpty;

    public IngredientCollectionDiff(@Nullable IIngredientCollection<T, M> additions,
                                    @Nullable IIngredientCollection<T, M> deletions, boolean completelyEmpty) {
        this.additions = additions;
        this.deletions = deletions;
        this.completelyEmpty = completelyEmpty;
    }

    @Nullable
    public IIngredientCollection<T, M> getAdditions() {
        return additions;
    }

    @Nullable
    public IIngredientCollection<T, M> getDeletions() {
        return deletions;
    }

    /**
     * @return If the (new) collection that was used to calculate this diff with was empty.
     */
    public boolean isCompletelyEmpty() {
        return completelyEmpty;
    }

    public boolean hasAdditions() {
        return getAdditions() != null && !getAdditions().isEmpty();
    }

    public boolean hasDeletions() {
        return getDeletions() != null && !getDeletions().isEmpty();
    }
}
