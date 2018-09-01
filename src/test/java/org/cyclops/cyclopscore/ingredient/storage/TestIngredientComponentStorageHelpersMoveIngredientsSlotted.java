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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestIngredientComponentStorageHelpersMoveIngredientsSlotted.SourceSlotless.DestinationSlotless.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlotted.SourceSlotless.DestinationSlotted.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlotted.SourceSlotted.DestinationSlotless.class,
        TestIngredientComponentStorageHelpersMoveIngredientsSlotted.SourceSlotted.DestinationSlotted.class,
})
@SuppressWarnings("Duplicates")
public class TestIngredientComponentStorageHelpersMoveIngredientsSlotted {

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

    private static IngredientCollectionPrototypeMap<ComplexStack, Integer> sourceSlotlessInnerStorage;
    private static IIngredientComponentStorage<ComplexStack, Integer> sourceSlotless;
    private static IngredientCollectionPrototypeMap<ComplexStack, Integer> destinationSlotlessInnerStorage;
    private static IIngredientComponentStorage<ComplexStack, Integer> destinationSlotless;

    private static IngredientList<ComplexStack, Integer> sourceSlottedInnerStorage;
    private static IIngredientComponentStorageSlotted<ComplexStack, Integer> sourceSlotted;
    private static IngredientList<ComplexStack, Integer> destinationSlottedInnerStorage;
    private static IIngredientComponentStorageSlotted<ComplexStack, Integer> destinationSlotted;

    public static void init() {
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
            public void beforeEach() {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClear() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceNotExtractable() {
                // Move nothing
                sourceSlotless = new IngredientComponentStorageCollectionWrapper<>(sourceSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableRate() {
                // Move nothing
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableMaxAmount() {
                // Move nothing
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotLoopDestinationSlotLoopQuantitativeLess() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopQuantitativeMore() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA010_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA010_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClearQuantitative() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA01_)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMoreQuantitative() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceExactQuantitative() {
                // Move 5
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

        }

        public static class DestinationSlotted {

            @Before
            public void beforeEach() {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined2() {
                // Move none
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test(expected = IndexOutOfBoundsException.class)
            public void testSourceSlotLoopDestinationSlotDefined5() {
                // Error
                IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 5,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true);
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, 0, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceClear() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFew() {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMore() {
                // Move 1
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceNotExtractable() {
                // Move nothing
                sourceSlotless = new IngredientComponentStorageCollectionWrapper<>(sourceSlotlessInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRate() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRate() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA08_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmount() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0QuantitativeLess() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA04_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceClearQuantitative() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFewQuantitative() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA01_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceExactQuantitative() {
                // Move 5
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRateQuantitative() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitative() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitativeButValidSource() {
                // Move nothing
                sourceSlotlessInnerStorage.clear();
                sourceSlotlessInnerStorage.add(CA05_);
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA05_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmountQuantitative() {
                // Move nothing
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotless, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlotlessInnerStorage), is(Sets.newHashSet(CA09_, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }
            
        }

    }

    public static class SourceSlotted {

        public static class DestinationSlotless {

            @Before
            public void beforeEach() {
                init();
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotDefined() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop0() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop3() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 3, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 3, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroup() {
                // Move 1 of tag B
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), is(CA01B));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), is(CA01B));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B, CA01B)));
            }

            @Test(expected = IndexOutOfBoundsException.class)
            public void testSourceSlotDefinedDestinationSlotLoop5() {
                // Error
                IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 5, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true);
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA06_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceClear() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupSourceClear() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceClear() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupSourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1DestinationNonInsertable() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupDestinationNonInsertable() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNonInsertable() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1DestinationNonInsertableMaxAmount() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop4MatchGroupDestinationNonInsertableMaxAmount() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 4, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNonInsertableMaxAmount() {
                destinationSlotless = new IngredientComponentStorageCollectionWrapper<>(destinationSlotlessInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop1SourceFewQuantitativeMatch() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA01_, CA91B)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitativeMatch() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(EMPTY);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotless, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)));
                assertThat(Sets.newHashSet(destinationSlotlessInnerStorage), is(Sets.newHashSet(CA02_, CA91B)));
            }

        }

        public static class DestinationSlotted {

            @Before
            public void beforeEach() {
                init();
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined0() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined0() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined0() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined1() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined1() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined1() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined1MatchGroup() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroup() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined1MatchGroup() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined1MatchGroup() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined2() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined2() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined2() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined2() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined0DestinationSlotDefined4() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test
            public void testSourceSlotDefined2DestinationSlotDefined4() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 2, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined4() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined4MatchGroup() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined4DestinationSlotDefined0MatchGroup() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA05_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined2() {
                // Move none
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 2,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA05_)));
            }

            @Test(expected = IndexOutOfBoundsException.class)
            public void testSourceSlotLoopDestinationSlotDefined5() {
                // Error
                IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 5,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true);
            }

            @Test
            public void testSourceSlotDefinedDestinationSlotLoop() {
                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 0, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoop() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroup() {
                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA05_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA05_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA06_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Start clear

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0Clear() {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1Clear() {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4Clear() {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopClear() {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupClear() {
                sourceSlottedInnerStorage.clear();

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet()));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source few start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceFew() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source more start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceMore() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source not extractable

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceNotExtractable() {
                sourceSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(sourceSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination not insertable rate

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationNotInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationNotInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationNotInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 0);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination fewer insertable rate

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupDestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationFewerInsertableRate() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move 5
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA08_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination not insertable max amount

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationNotInsertableMaxAmount() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationNotInsertableMaxAmount() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationNotInsertableMaxAmount() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationNotInsertableMaxAmount() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationNotInsertableMaxAmount() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 0, 10);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            /* The following tests include an exact quantity matcher flag */

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceFewQuantitativeOk() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceFewQuantitativeOk() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, EMPTY)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceFewQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA01_);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA01_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Source more start

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupSourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, CA01_, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4SourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, CA01_)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopSourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupSourceMoreQuantitative() {
                sourceSlottedInnerStorage.clear();
                sourceSlottedInnerStorage.add(EMPTY);
                sourceSlottedInnerStorage.add(CA05_);

                // Move 1
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), is(CA01_));
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA01_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), is(CA01_));

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA04_)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA02_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            // Destination fewer insertable rate

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined0DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined1MatchGroupDestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotDefined1DestinationSlotDefined4DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, 1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined0DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 0,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined1DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotDefined4DestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, 4,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopDestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }

            @Test
            public void testSourceSlotLoopDestinationSlotLoopMatchGroupDestinationFewerInsertableRateQuantitative() {
                destinationSlotted = new IngredientComponentStorageSlottedCollectionWrapper<>(destinationSlottedInnerStorage, 100, 1);

                // Move nothing
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, true), nullValue());
                assertThat(IngredientStorageHelpers.moveIngredientsSlotted(sourceSlotted, -1, destinationSlotted, -1,
                        CA05_, ComplexStack.Match.GROUP | ComplexStack.Match.TAG | ComplexStack.Match.AMOUNT, false), nullValue());

                assertThat(Sets.newHashSet(sourceSlottedInnerStorage), is(Sets.newHashSet(EMPTY, CA09_, EMPTY, CB02_, CA01B)));
                assertThat(Lists.newArrayList(destinationSlotted), is(Lists.newArrayList(CA01_, EMPTY, CA91B, EMPTY, EMPTY)));
            }
            
        }
        
    }

}
