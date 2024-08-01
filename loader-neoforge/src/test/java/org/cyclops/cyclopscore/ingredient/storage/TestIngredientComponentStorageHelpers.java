package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

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
    public void testMoveIngredientsNoneEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsNoneNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsFittingRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsFittingRateNonEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsHigherThanRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsHigherThanRateNonEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsHigherThanContentsNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(5, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, true), is(5));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsHigherThanMaxEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsHigherThanMaxNonEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsNonEmptySourceBlocked() throws InconsistentIngredientInsertionException {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsNonEmptyDestinationBlocked() throws InconsistentIngredientInsertionException {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchNoneEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchNoneNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateNonEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsMatchHigherThanRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanRateNonEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsMatchHigherThanContentsNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(5, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, true), is(5));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, false), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanMaxEmpty() throws InconsistentIngredientInsertionException {
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
    public void testMoveIngredientsMatchHigherThanMaxNonEmpty() throws InconsistentIngredientInsertionException {
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

    @Test
    public void testMoveIngredientsMatchNonEmptySourceBlocked() throws InconsistentIngredientInsertionException {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchNonEmptyDestinationBlocked() throws InconsistentIngredientInsertionException {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateNoneEmpty() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> false;
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateNoneNonEmpty() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> false;
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateAllEmpty() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmpty() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptySourceBlocked() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyDestinationBlocked() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateNoneEmptyLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> false;
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateNoneNonEmptyLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> false;
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateAllEmptyLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(5));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(95)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyLowerQuantityExact() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, true), is(5));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, false), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(95)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyHigherQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        sourceStorage.insert(1, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(1));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(1)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyHigherQuantityExact() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        sourceStorage.insert(1, false);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(1)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptySourceBlockedLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyDestinationBlockedLowerQuantity() throws InconsistentIngredientInsertionException {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeNoneEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeNoneNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeFittingRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeFittingRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false), is(20));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(80)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(20)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateUnevenNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, false), is(11));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(89)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(11)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanMaxEmpty() throws InconsistentIngredientInsertionException {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true), is(0));
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanMaxNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true), is(9));
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true), is(1));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchNoneEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchNoneNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchFittingRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchFittingRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, false), is(20));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(80)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(20)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanMaxEmpty() throws InconsistentIngredientInsertionException {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, true), is(0));
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, false), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanMaxNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, true), is(9));
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, false), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, true), is(1));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, false), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactNoneEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactNoneNonEmpty() throws InconsistentIngredientInsertionException {
        sourceStorage.insert(100, false);

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, false), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactFittingRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactFittingRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, false), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanFittingRateUnevenNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, false, true), is(10));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, false, false), is(11));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(89)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(11)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanRateEmpty() throws InconsistentIngredientInsertionException {
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanRateNonEmpty() throws InconsistentIngredientInsertionException {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanMaxEmpty() throws InconsistentIngredientInsertionException {
        for (int i = 0; i <= 10; i++) {
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, true), is(0));
            assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, false), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, true), is(0));
        assertThat(IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, false), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

}
