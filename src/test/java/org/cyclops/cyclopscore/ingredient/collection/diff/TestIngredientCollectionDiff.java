package org.cyclops.cyclopscore.ingredient.collection.diff;

import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollection;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionEmpty;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionDiff {

    private final IIngredientCollection<Integer, Boolean> collection1 =
            new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3, 4);
    private final IIngredientCollection<Integer, Boolean> collection2 =
            new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 1, 2);
    private final IIngredientCollection<Integer, Boolean> collectionEmpty =
            new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE);

    @Test
    public void testGetAdditions() {
        IngredientCollectionDiff<Integer, Boolean> diff1 = new IngredientCollectionDiff<>(collection1, collection2, false);
        assertThat(diff1.getAdditions(), is (collection1));
    }

    @Test
    public void testGetDeletions() {
        IngredientCollectionDiff<Integer, Boolean> diff1 = new IngredientCollectionDiff<>(collection1, collection2, false);
        assertThat(diff1.getDeletions(), is (collection2));
    }

    @Test
    public void testHasAdditions() {
        assertThat(new IngredientCollectionDiff<>(collection1, collection2, false).hasAdditions(), is (true));
        assertThat(new IngredientCollectionDiff<>(null, collection2, false).hasAdditions(), is (false));
        assertThat(new IngredientCollectionDiff<>(collectionEmpty, collection2, false).hasAdditions(), is (false));
    }

    @Test
    public void testHasDeletions() {
        assertThat(new IngredientCollectionDiff<>(collection1, collection2, false).hasDeletions(), is (true));
        assertThat(new IngredientCollectionDiff<>(collection1, null, false).hasDeletions(), is (false));
        assertThat(new IngredientCollectionDiff<>(collection1, collectionEmpty, false).hasDeletions(), is (false));
    }

    @Test
    public void testIsCompletelyEmpty() {
        assertThat(new IngredientCollectionDiff<>(collection1, collection2, false).isCompletelyEmpty(), is (false));
        assertThat(new IngredientCollectionDiff<>(collection1, collection2, true).isCompletelyEmpty(), is (true));
    }

}
