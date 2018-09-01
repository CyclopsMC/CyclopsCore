package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageCollectionWrapper {

    private IngredientComponentStorageCollectionWrapper<Integer, Boolean> storage;

    @Before
    public void beforeEach() {
        storage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testGetComponent() {
        assertThat(storage.getComponent(), is(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testIterator() {
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList()));

        storage.insert(95, false);
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(95)));

        storage.insert(5, false);
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(100)));
    }

    @Test
    public void testIteratorMatch() {
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList()));

        storage.insert(95, false);
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList()));

        storage.insert(5, false);
        assertThat(Lists.newArrayList(storage.iterator(100, true)), is(Lists.newArrayList(100)));
    }

    @Test
    public void testGetMaxQuantity() {
        assertThat(storage.getMaxQuantity(), is(Long.MAX_VALUE));

        IngredientComponentStorageCollectionWrapper<Integer, Boolean> sizedStorage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE), 100, 11);

        assertThat(sizedStorage.getMaxQuantity(), is(100L));
    }

    @Test
    public void testRateLimit() {
        assertThat(storage.rateLimit(0, Long.MAX_VALUE), is(0));
        assertThat(storage.rateLimit(10, Long.MAX_VALUE), is(10));
        assertThat(storage.rateLimit(20, Long.MAX_VALUE), is(20));
        assertThat(storage.rateLimit(Integer.MAX_VALUE - 1, Long.MAX_VALUE), is(Integer.MAX_VALUE - 1));
        assertThat(storage.rateLimit(Integer.MAX_VALUE, Long.MAX_VALUE), is(Integer.MAX_VALUE));
    }

    @Test
    public void testRateLimitWithLimits() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE), 100, 11);

        assertThat(storage.rateLimit(0, 100), is(0));
        assertThat(storage.rateLimit(10, 100), is(10));
        assertThat(storage.rateLimit(20, 100), is(11));
        assertThat(storage.rateLimit(Integer.MAX_VALUE - 1, 100), is(11));
        assertThat(storage.rateLimit(Integer.MAX_VALUE, 100), is(11));

        assertThat(storage.rateLimit(20, 5), is(5));
    }

    @Test
    public void testInsert() {
        assertThat(storage.insert(10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));

        assertThat(storage.insert(10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10)));

        assertThat(storage.insert(100, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10)));

        assertThat(storage.insert(100, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(110)));
    }

    @Test
    public void testInsertWithLimits() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE), 9, 5);

        assertThat(storage.insert(10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));

        assertThat(storage.insert(10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));

        assertThat(storage.insert(100, true), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));

        assertThat(storage.insert(100, false), is(96));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9)));

        assertThat(storage.insert(100, true), is(100));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9)));

        assertThat(storage.insert(100, false), is(100));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9)));
    }

    @Test
    public void testExtract() {
        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100)));

        assertThat(storage.extract(10, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));
    }

    @Test
    public void testExtractWithLimits() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE), 9, 5);
        assertThat(storage.insert(5, false), is(0));
        assertThat(storage.insert(4, false), is(0));

        assertThat(storage.extract(10, true), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(9)));

        assertThat(storage.extract(10, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(4)));

        assertThat(storage.extract(10, true), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(4)));

        assertThat(storage.extract(10, false), is(4));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));
    }

    @Test
    public void testExtractMatch() {
        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, true, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100)));

        assertThat(storage.extract(10, true, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, true, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, true, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(89, true, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(89, true, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));


        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, false, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100)));

        assertThat(storage.extract(10, false, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, false, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90)));

        assertThat(storage.extract(1, false, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, false, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89)));

        assertThat(storage.extract(100, false, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));
    }

    @Test
    public void testExtractMatchWithLimits() {
        IngredientComponentStorageCollectionWrapper<Integer, Boolean> storage = new IngredientComponentStorageCollectionWrapper<>(
                new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE), 4, 2);
        assertThat(storage.insert(2, false), is(0));
        assertThat(storage.insert(2, false), is(0));

        assertThat(storage.extract(4, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(4)));

        assertThat(storage.extract(4, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(4)));

        assertThat(storage.extract(2, false, true), is(2));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(4)));

        assertThat(storage.extract(2, false, false), is(2));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(2)));
    }

}
