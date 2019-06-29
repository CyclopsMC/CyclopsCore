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
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageHelpersComplex {

    private IngredientCollectionQuantitativeGrouper<ComplexStack, Integer, IIngredientListMutable<ComplexStack, Integer>> sourceInnerStorage;
    private IIngredientComponentStorage<ComplexStack, Integer> sourceStorage;
    private static IngredientList<ComplexStack, Integer> destinationSlottedInnerStorage;
    private static IIngredientComponentStorageSlotted<ComplexStack, Integer> destinationSlotted;

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CB01_ = new ComplexStack(ComplexStack.Group.B, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);

    @Before
    public void beforeEach() {
        sourceInnerStorage = new IngredientCollectionQuantitativeGrouper<>(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX));
        sourceStorage = new IngredientComponentStorageCollectionWrapper<>(sourceInnerStorage, 100, 10);
        destinationSlottedInnerStorage = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 10);
    }

    // The iterator will first return A, and then B

    @Test
    public void testMoveIngredientsMatchDestinationFilterATag() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterATagGroup() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterATagGroupAmount() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CA01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTag() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, true), is(CB01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, false), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTagGroup() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CB01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationFilterBTagGroupAmount() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(CB01_);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CB01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB02_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATag() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.TAG, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATagGroup() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterATagGroupAmount() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTag() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, true), is(CA01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.TAG, false), is(CA01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CB01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTagGroup() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CB01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
    }

    @Test
    public void testMoveIngredientsMatchDestinationNoFilterBTagGroupAmount() throws InconsistentIngredientInsertionException {
        destinationSlottedInnerStorage.add(null);

        sourceInnerStorage.add(CA01_);
        sourceInnerStorage.add(CB01_);

        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CB01_));
        assertThat(IngredientStorageHelpers.moveIngredients(sourceStorage, destinationSlotted, CB01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CB01_));

        assertThat(Lists.newArrayList(sourceInnerStorage), is(Lists.newArrayList(CA01_)));
        assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CB01_)));
    }

}
