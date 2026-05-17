package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageHelpers {

    private IngredientCollectionPrototypeMap<Integer, Boolean> sourceInnerStorage;
    private IIngredientComponentStorage<Integer, Boolean> sourceStorage;
    private IngredientCollectionPrototypeMap<Integer, Boolean> destinationInnerStorage;
    private IIngredientComponentStorage<Integer, Boolean> destinationStorage;

    @BeforeEach
    public void beforeEach() {
        sourceInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        sourceStorage = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 10);
        destinationInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        destinationStorage = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 10);
    }

    @Test
    public void testInsertIngredientQuantityNone() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 0, tx)), is(0L));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 0, tx)), is(0L));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testInsertIngredientQuantityFittingRate() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(10L));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(10L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientQuantityHigherThanRate() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 20, tx)), is(10L));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 20, tx)), is(10L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientQuantityHigherThanMaxQuantity() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 9, tx)), is(9L));
            assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 9, tx)), is(9L));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(1L));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(1L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(0L));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredientQuantity(destinationStorage, 10, tx)), is(0L));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testInsertIngredientNone() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 0, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 0, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testInsertIngredientFittingRate() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(10));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientHigherThanRate() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 20, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 20, tx)), is(10));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testInsertIngredientHigherThanMax() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 9, tx)), is(9));
            assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 9, tx)), is(9));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }
        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(1));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.insertIngredient(destinationStorage, 10, tx)), is(0));

        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsNoneEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsFittingRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsHigherThanRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsHigherThanContentsNonEmpty() {
        sourceStorage.insert(5, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(5));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, tx)), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

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
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(9));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsNonEmptySourceBlocked() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsNonEmptyDestinationBlocked() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchNoneEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 0, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 20, false, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanContentsNonEmpty() {
        sourceStorage.insert(5, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(5));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 10, false, tx)), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsMatchHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

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
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(9));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsMatchNonEmptySourceBlocked() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsMatchNonEmptyDestinationBlocked() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateNoneEmpty() {
        Predicate<Integer> predicate = (i) -> false;
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateNoneNonEmpty() {
        Predicate<Integer> predicate = (i) -> false;
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateAllEmpty() {
        Predicate<Integer> predicate = (i) -> true;

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmpty() {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptySourceBlocked() {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyDestinationBlocked() {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, Integer.MAX_VALUE, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateNoneEmptyLowerQuantity() {
        Predicate<Integer> predicate = (i) -> false;
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateNoneNonEmptyLowerQuantity() {
        Predicate<Integer> predicate = (i) -> false;
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsPredicateAllEmptyLowerQuantity() {
        Predicate<Integer> predicate = (i) -> true;

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyLowerQuantity() {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(5));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(95)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyLowerQuantityExact() {
        Predicate<Integer> predicate = (i) -> true;
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, tx)), is(5));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, tx)), is(5));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(95)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyHigherQuantity() {
        Predicate<Integer> predicate = (i) -> true;
        sourceStorage.insert(1, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(1)));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyHigherQuantityExact() {
        Predicate<Integer> predicate = (i) -> true;
        sourceStorage.insert(1, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(1)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptySourceBlockedLowerQuantity() {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sourceStorage
                = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsPredicateAllNonEmptyDestinationBlockedLowerQuantity() {
        Predicate<Integer> predicate = (i) -> true;
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> destinationStorage
                = new IngredientComponentStorageCollectionWrapper<>(destinationInnerStorage, 100, 0);
        sourceInnerStorage.add(100);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, predicate, 5, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeNoneEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeFittingRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, tx)), is(20));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, tx)), is(20));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(80)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(20)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanRateUnevenNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, tx)), is(11));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, tx)), is(11));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(89)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(11)));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeHigherThanMaxNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(9));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, tx)), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchNoneEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, false, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchFittingRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, false, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, tx)), is(20));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, false, tx)), is(20));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(80)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(20)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanMaxNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(9));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(9));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100 - (i + 1) * 9)));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList((i + 1) * 9)));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(1));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, false, tx)), is(1));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationStorage, 9, false, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(100)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactNoneEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactNoneNonEmpty() {
        sourceStorage.insert(100, false);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 0, true, tx)), is(0));

        assertThat(destinationInnerStorage.isEmpty(), is(true));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactFittingRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactFittingRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, tx)), is(10));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 10, true, tx)), is(10));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(90)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(10)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchHigherThanFittingRateUnevenNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, false, tx)), is(11));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 11, false, tx)), is(11));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(89)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList(11)));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanRateEmpty() {
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanRateNonEmpty() {
        int toInsert = 100;
        while (toInsert > 0) {
            toInsert -= sourceStorage.insert(toInsert, false);
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 20, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }

    @Test
    public void testMoveIngredientsIterativeMatchExactHigherThanMaxEmpty() {
        for (int i = 0; i <= 10; i++) {
            assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));
            assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));

            assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
            assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
        }

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredientsIterative(sourceStorage, destinationStorage, 9, true, tx)), is(0));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList()));
        assertThat(Lists.newArrayList(destinationInnerStorage), is(Lists.newArrayList()));
    }


    private static <T> T simulateTx(java.util.function.Function<TransactionContext, T> fn) {
        try (var tx = Transaction.openRoot()) {
            return fn.apply(tx);
        }
    }

    private static <T> T executeTx(java.util.function.Function<TransactionContext, T> fn) {
        try (var tx = Transaction.openRoot()) {
            T result = fn.apply(tx);
            tx.commit();
            return result;
        }
    }

}
