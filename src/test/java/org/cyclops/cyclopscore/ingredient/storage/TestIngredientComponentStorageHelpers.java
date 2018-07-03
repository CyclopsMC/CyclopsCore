package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageHelpers {

    private IngredientCollectionPrototypeMap<Integer, Boolean> sourceInnerStorage;
    private IIngredientComponentStorage<Integer, Boolean> sourceStorage;
    private IngredientCollectionPrototypeMap<Integer, Boolean> destinationInnerStorage;
    private IIngredientComponentStorage<Integer, Boolean> destinationStorage;

    @Before
    public void beforeEach() {
        sourceInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        sourceStorage = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 10);
        destinationInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        destinationStorage = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 10);
    }

    @Test
    public void testInsertIngredientQuantityNone() {
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 0, true), is(0L));
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 0, false), is(0L));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testInsertIngredientQuantityFittingRate() {
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, true), is(10L));
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, false), is(10L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientQuantityHigherThanRate() {
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 20, true), is(10L));
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 20, false), is(10L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientQuantityHigherThanMaxQuantity() {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 9, true), is(9L));
            assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 9, false), is(9L));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, true), is(1L));
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, false), is(1L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, true), is(0L));
        assertThat(IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, false), is(0L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testInsertIngredientNone() {
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testInsertIngredientFittingRate() {
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, true), is(10));
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, false), is(10));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientHigherThanRate() {
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 20, true), is(10));
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 20, false), is(10));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientHigherThanMax() {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 9, true), is(9));
            assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 9, false), is(9));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, true), is(1));
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, false), is(1));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, true), is(0));
        assertThat(IngredientStorageHelpers.insertIngredient(destinationStorage, 10, false), is(0));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsNoneEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsFittingRateEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsHigherThanRateEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsHigherThanMaxNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(9));
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(1));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsMatchNoneEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanRateEmpty() {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanMaxNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(9));
            assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(1));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

}
