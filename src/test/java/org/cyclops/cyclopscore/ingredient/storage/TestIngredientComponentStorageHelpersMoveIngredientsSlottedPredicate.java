package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageSlotted;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.cyclops.cyclopscore.ingredient.collection.IngredientList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestIngredientComponentStorageHelpersMoveIngredientsSlottedPredicate.SourceSlotless.DestinationSlotless.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlottedPredicate.SourceSlotless.DestinationSlotted.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlottedPredicate.SourceSlotted.DestinationSlotless.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlottedPredicate.SourceSlotted.DestinationSlotted.class,
})
@SuppressWarnings("Duplicates")
public class TestIngredientComponentStorageHelpersMoveIngredientsSlottedPredicate {

    private static final ComplexStack EMPTY = IngredientComponentStubs.COMPLEX.getMatcher().getEmptyInstance();

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CA02_ = new ComplexStack(ComplexStack.Group.A, 0, 2, null);
    private static final ComplexStack CA03_ = new ComplexStack(ComplexStack.Group.A, 0, 3, null);
    private static final ComplexStack CA04_ = new ComplexStack(ComplexStack.Group.A, 0, 4, null);
    private static final ComplexStack CA05_ = new ComplexStack(ComplexStack.Group.A, 0, 5, null);
    private static final ComplexStack CA06_ = new ComplexStack(ComplexStack.Group.A, 0, 6, null);
    private static final ComplexStack CA07_ = new ComplexStack(ComplexStack.Group.A, 0, 7, null);
    private static final ComplexStack CA08_ = new ComplexStack(ComplexStack.Group.A, 0, 8, null);
    private static final ComplexStack CA09_ = new ComplexStack(ComplexStack.Group.A, 0, 9, null);
    private static final ComplexStack CA010_ = new ComplexStack(ComplexStack.Group.A, 0, 10, null);

    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    private static final Predicate<ComplexStack> P_GROUP_TAG = (i) -> i.getGroup() == ComplexStack.Group.A && i.getTag() == null;
    private static final Predicate<ComplexStack> P_GROUP = (i) -> i.getGroup() == ComplexStack.Group.A;

    private static IngredientCollectionPrototypeMap<ComplexStack, Integer> sourceSlotlessInnerStorage;
    private static IIngredientComponentStorage<ComplexStack, Integer> sourceSlotless;
    private static IngredientCollectionPrototypeMap<ComplexStack, Integer> destinationSlotlessInnerStorage;
    private static IIngredientComponentStorage<ComplexStack, Integer> destinationSlotless;

    private static IngredientList<ComplexStack, Integer> sourceSlottedInnerStorage;
    private static IIngredientComponentStorageSlotted<ComplexStack, Integer> sourceSlotted;
    private static IngredientList<ComplexStack, Integer> destinationSlottedInnerStorage;
    private static IIngredientComponentStorageSlotted<ComplexStack, Integer> destinationSlotted;

    public static void init() throws InconsistentIngredientInsertionException {
        /* Slotless */
        sourceSlotlessInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.COMPLEX);
        sourceSlotless = new IngredientComponentStorageCollectionWrapper<>(sourceSlotlessInnerStorage, 100, 10);
        destinationSlotlessInnerStorage = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.COMPLEX);
        destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 10);

        sourceSlotlessInnerStorage.add(CA09_);
        sourceSlotlessInnerStorage.add(CB02_);
        sourceSlotlessInnerStorage.add(CA01B);
        destinationSlotlessInnerStorage.add(CA01_);
        destinationSlotlessInnerStorage.add(CA91B);
        
        /* Slotted */
        sourceSlottedInnerStorage = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 10);
        destinationSlottedInnerStorage = new IngredientArrayList<>(IngredientComponentStubs.COMPLEX);
        destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 10);

        sourceSlottedInnerStorage.addAll(Lists.newArrayList(
                EMPTY, CA09_, EMPTY, CB02_, CA01B));
        destinationSlottedInnerStorage.addAll(Lists.newArrayList(
                CA01_, EMPTY, CA91B, EMPTY, EMPTY));
    }

    public static class SourceSlotless {

        public static class DestinationSlotless {

            @Before
            public void beforeEach() throws InconsistentIngredientInsertionException {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClear() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() throws InconsistentIngredientInsertionException {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceNotExtractable() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotless = new IngredientComponentStorageCollectionWrapper<>(sourceSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotLoopDestinationSlotLoopQuantitativeLess() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopQuantitativeMore() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 10, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 10, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClearQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA01_)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceExactQuantitative() throws InconsistentIngredientInsertionException {
                // Move 5
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceExactNotEnoughRoom() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                destinationSlotlessInnerStorage.clear();
                IngredientComponentStorageCollectionWrapper<ComplexStack, Integer> destinationSlotless =
                        new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 1, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA05_)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet()));
            }

        }

        public static class DestinationSlotted {

            @Before
            public void beforeEach() throws InconsistentIngredientInsertionException {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move none
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined5OutOfBounds() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 5,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 5,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceClear() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFew() throws InconsistentIngredientInsertionException {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMore() throws InconsistentIngredientInsertionException {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceNotExtractable() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotless = new IngredientComponentStorageCollectionWrapper<>(sourceSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA08_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0QuantitativeLess() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceClearQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceExactQuantitative() throws InconsistentIngredientInsertionException {
                // Move 5
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitativeButValidSource() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA05_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmountQuantitative() throws InconsistentIngredientInsertionException {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0LoopSourceExactNotEnoughRoom() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 5, 64);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA05_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopLoopSourceExactNotEnoughRoom() throws InconsistentIngredientInsertionException {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 5, 64);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }
            
        }

    }

    public static class SourceSlotted {

        public static class DestinationSlotless {

            @Before
            public void beforeEach() throws InconsistentIngredientInsertionException {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop0() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop3() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 3, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 3, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroup() throws InconsistentIngredientInsertionException {
                // Move 1 of tag B
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), is(CA01B));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), is(CA01B));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B, CA01B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop5OutOfBounds() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 5, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 5, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceClear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupSourceClear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupSourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1DestinationNonInsertable() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupDestinationNonInsertable() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNonInsertable() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1DestinationNonInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupDestinationNonInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNonInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFewQuantitativeMatch() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitativeMatch() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceExactJustEnoughRoom() throws InconsistentIngredientInsertionException {
                destinationSlotlessInnerStorage = new IngredientCollectionPrototypeMap<ComplexStack, Integer>(IngredientComponentStubs.COMPLEX) {
                    @Override
                    public boolean add(ComplexStack instance) {
                        boolean added = super.add(instance);
                        if (added && this.size() > 2) {
                            this.remove(instance);
                            return false;
                        }
                        return added;
                    }
                };
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 11, 10);
                destinationSlotless.insert(CA01_, false);
                destinationSlotless.insert(CA91B, false);

                // Move 9
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 9, true, true), is(CA09_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 9, true, false), is(CA09_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA010_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceExactNotEnoughRoom() throws InconsistentIngredientInsertionException {
                destinationSlotlessInnerStorage = new IngredientCollectionPrototypeMap<ComplexStack, Integer>(IngredientComponentStubs.COMPLEX) {
                    @Override
                    public boolean add(ComplexStack instance) {
                        boolean added = super.add(instance);
                        if (added && this.size() > 2) {
                            this.remove(instance);
                            return false;
                        }
                        return added;
                    }
                };
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 10, 10);
                destinationSlotless.insert(CA01_, false);
                destinationSlotless.insert(CA91B, false);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 9, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        P_GROUP_TAG, 9, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

        }

        public static class DestinationSlotted {

            @Before
            public void beforeEach() throws InconsistentIngredientInsertionException {
                init();
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined1MatchGroup() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroup() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined1MatchGroup() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined1MatchGroup() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined4MatchGroup() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        P_GROUP, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined0MatchGroup() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined2() throws InconsistentIngredientInsertionException {
                // Move none
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 2,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined5OutOfBounds() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 5,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 5,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroup() throws InconsistentIngredientInsertionException {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Start clear

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0Clear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1Clear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4Clear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopClear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupClear() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source few start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceFew() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source more start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceMore() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source not extractable

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceNotExtractable() throws InconsistentIngredientInsertionException {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination not insertable rate

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationNotInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination fewer insertable rate

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupDestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationFewerInsertableRate() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination not insertable max amount

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationNotInsertableMaxAmount() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, false, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFewQuantitativeOk() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFewQuantitativeOk() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceFewQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source more start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 1, false, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 1, false, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceMoreQuantitative() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, true, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 1, true, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination fewer insertable rate

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupDestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        P_GROUP, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationFewerInsertableRateQuantitative() throws InconsistentIngredientInsertionException {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        P_GROUP_TAG, 5, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceJustEnoughRoom() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA09_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 10, 10);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 9, true, true), is(CA09_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 9, true, false), is(CA09_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA010_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceNotEnoughRoom() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA09_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 9, 10);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 9, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        P_GROUP_TAG, 9, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotLoopSourceJustEnoughRoom() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA09_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 9, 10);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, -1,
                        P_GROUP_TAG, 9, true, true), is(CA09_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, -1,
                        P_GROUP_TAG, 9, true, false), is(CA09_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA09_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotLoopSourceNotEnoughRoom() throws InconsistentIngredientInsertionException {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA09_);

                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 8, 10);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, -1,
                        P_GROUP_TAG, 9, true, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, -1,
                        P_GROUP_TAG, 9, true, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }
            
        }
        
    }

}
