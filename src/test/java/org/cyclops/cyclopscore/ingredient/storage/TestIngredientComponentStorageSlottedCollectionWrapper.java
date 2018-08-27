package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageSlottedCollectionWrapper {

    private IngredientArrayList<Integer, Boolean> innerList;
    private IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage;

    @Before
    public void beforeEach() {
        innerList = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE);
        storage = new IngredientComponentStorageSlottedCollectionWrapper<>(innerList, 100, Long.MAX_VALUE);

        innerList.add(0);
        innerList.add(0);
        innerList.add(0);
        innerList.add(0);
        innerList.add(0);
    }

    @Test
    public void testGetComponent() {
        assertThat(storage.getComponent(), is(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testIterator() {
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        storage.insert(1, 95, false);
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(0, 95, 0, 0, 0)));

        storage.insert(2, 5, false);
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(0, 95, 5, 0, 0)));

        storage.insert(1, 5, false);
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(0, 100, 5, 0, 0)));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList()));

        storage.insert(1, 95, false);
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList()));

        storage.insert(1, 5, false);
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList(100)));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(storage.getMaxQuantity(), is(500L));

        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> sizedStorage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 10, 11);

        assertThat(sizedStorage.getMaxQuantity(), is(50L));
    }

    @Test
    public void testGetMaxSlotQuantity() {
        assertThat(storage.getMaxQuantity(0), is(100L));

        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> sizedStorage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 10, 11);

        assertThat(sizedStorage.getMaxQuantity(0), is(10L));
    }

    @Test
    public void testInsertSlotted() {
        assertThat(storage.insert(0, 0, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 0, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 100, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 100, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 0, 0, 0)));

        assertThat(storage.insert(2, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 0, 0, 0)));

        assertThat(storage.insert(2, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 10, 0, 0)));

        assertThat(storage.insert(2, 100, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 10, 0, 0)));

        assertThat(storage.insert(2, 100, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 100, 0, 0)));
    }

    @Test
    public void testInsertSlottedWithLimits() {
        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 9, 5);

        assertThat(storage.insert(0, 10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 10, true), is(6));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5, 0, 0, 0, 0)));

        assertThat(storage.insert(0, 10, false), is(6));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 0, 0, 0)));

        assertThat(storage.insert(2, 10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 0, 0, 0)));

        assertThat(storage.insert(2, 10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 5, 0, 0)));

        assertThat(storage.insert(2, 100, true), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 5, 0, 0)));

        assertThat(storage.insert(2, 100, false), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 9, 0, 0)));
    }

    @Test
    public void testGetSlotContents() {
        assertThat(storage.getSlotContents(0), is(0));
        assertThat(storage.getSlotContents(1), is(0));
        assertThat(storage.getSlotContents(2), is(0));
        assertThat(storage.getSlotContents(3), is(0));
        assertThat(storage.getSlotContents(4), is(0));

        assertThat(storage.insert(1, 10, false), is(0));

        assertThat(storage.getSlotContents(0), is(0));
        assertThat(storage.getSlotContents(1), is(10));
        assertThat(storage.getSlotContents(2), is(0));
        assertThat(storage.getSlotContents(3), is(0));
        assertThat(storage.getSlotContents(4), is(0));

        assertThat(storage.insert(3, 100, false), is(0));

        assertThat(storage.getSlotContents(0), is(0));
        assertThat(storage.getSlotContents(1), is(10));
        assertThat(storage.getSlotContents(2), is(0));
        assertThat(storage.getSlotContents(3), is(100));
        assertThat(storage.getSlotContents(4), is(0));
    }

    @Test
    public void testExtractSlottedEmpty() {
        assertThat(storage.extract(0, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.extract(0, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testExtractSlotted() {
        assertThat(storage.insert(1, 100, false), is(0));

        assertThat(storage.extract(0, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(0, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(0, 1, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(0, 1, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(0, 100, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(0, 100, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(1, 10, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(1, 10, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, 1, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, 1, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(1, 100, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(1, 100, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testExtractSlottedWithLimits() {
        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 9, 5);
        assertThat(storage.insert(1, 5, false), is(0));
        assertThat(storage.insert(1, 4, false), is(0));

        assertThat(storage.extract(0, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(0, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(0, 10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(0, 10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(1, 10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(1, 10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(1, 10, true), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(1, 10, false), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testInsert() {
        assertThat(storage.insert(0, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(0, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 0, 0, 0, 0)));

        assertThat(storage.insert(100, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 0, 0, 0, 0)));

        assertThat(storage.insert(100, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 0, 0, 0, 0)));
    }

    @Test
    public void testInsertWithLimits() {
        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 9, 5);

        assertThat(storage.insert(10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));

        assertThat(storage.insert(10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5, 0, 0, 0, 0)));

        assertThat(storage.insert(100, true), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5, 0, 0, 0, 0)));

        assertThat(storage.insert(100, false), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 0, 0, 0)));

        assertThat(storage.insert(100, true), is(95));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 0, 0, 0, 0)));

        assertThat(storage.insert(100, false), is(95));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9, 5, 0, 0, 0)));
    }

    @Test
    public void testExtract() {
        assertThat(storage.insert(1, 100, false), is(0));

        assertThat(storage.extract(10, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(10, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(100, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(100, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testExtractEmpty() {
        innerList.clear();

        assertThat(storage.extract(10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));

        assertThat(storage.extract(10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));
    }

    @Test
    public void testExtractWithLimits() {
        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 9, 5);
        assertThat(storage.insert(1, 5, false), is(0));
        assertThat(storage.insert(1, 4, false), is(0));

        assertThat(storage.extract(10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 9, 0, 0, 0)));

        assertThat(storage.extract(10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(10, true), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(10, false), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testExtractMatch() {
        assertThat(storage.insert(1, 100, false), is(0));

        assertThat(storage.extract(10, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(10, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(1, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(1, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(100, true, true), is(100));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(100, true, false), is(100));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));


        assertThat(storage.insert(1, 100, false), is(0));

        assertThat(storage.extract(10, false, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 100, 0, 0, 0)));

        assertThat(storage.extract(10, false, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, false, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 90, 0, 0, 0)));

        assertThat(storage.extract(1, false, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(100, false, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 89, 0, 0, 0)));

        assertThat(storage.extract(100, false, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 0, 0, 0, 0)));
    }

    @Test
    public void testExtractMatchWithLimits() {
        IngredientComponentStorageSlottedCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageSlottedCollectionWrapper<>(
                innerList, 4, 2);
        assertThat(storage.insert(1, 2, false), is(0));
        assertThat(storage.insert(1, 2, false), is(0));

        assertThat(storage.extract(4, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(4, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(2, false, true), is(2));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 4, 0, 0, 0)));

        assertThat(storage.extract(2, false, false), is(2));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(0, 2, 0, 0, 0)));
    }

}
