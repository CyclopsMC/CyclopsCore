package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientListMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionQuantitativeGrouper;
import org.cyclops.cyclopscore.ingredient.collection.IngredientList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageHelpersComplex {

    private IngredientCollectionQuantitativeGrouper<ComplexStack, Integer, IIngredientListMutable<ComplexStack, Integer>> sourceInnerStorage;
    private IIngredientComponentStorage<ComplexStack, Integer> sourceStorage;
    private IngredientList<ComplexStack, Integer> destinationSlottedInnerStorage;
    private IIngredientComponentStorageSlotted<ComplexStack, Integer> destinationSlotted;

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CB01_ = new ComplexStack(ComplexStack.Group.B, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);

    @BeforeEach
    public void beforeEach() {
        sourceInnerStorage = new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX));
        sourceStorage = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 10);
        destinationSlottedInnerStorage = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 10);
    }

    // The iterator will first return A, and then B

    @Test
    public void testMoveIngredientsMatchDestinationFilterATag() {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, tx)), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterATagGroup() {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterATagGroupAmount() {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTag() {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, tx)), is(CB01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTagGroup() {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CB01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTagGroupAmount() {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CB01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATag() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        // Simulate and non-simulate produce different results, as IngredientComponentStorageCollectionWrapper is unsafe!
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATagGroup() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATagGroupAmount() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTag() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        // Simulate and non-simulate produce different results, as IngredientComponentStorageCollectionWrapper is unsafe!
        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, tx)), is(CA01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTagGroup() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CB01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTagGroupAmount() {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(simulateTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CB01_));
        assertThat(executeTx(tx -> IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, tx)), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
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
