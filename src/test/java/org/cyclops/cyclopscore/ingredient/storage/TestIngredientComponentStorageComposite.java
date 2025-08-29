package org.cyclops.cyclopscore.ingredient.storage;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionPrototypeMap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientComponentStorageComposite {

    private IngredientCollectionPrototypeMap<Integer, Boolean> s1;
    private IngredientCollectionPrototypeMap<Integer, Boolean> s2;
    private IngredientComponentStorageComposite<Integer, Boolean> storage;

    @Before
    public void beforeEach() {
        s1 = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        s2 = new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.SIMPLE);
        s2.add(5);
        storage = new IngredientComponentStorageComposite<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(
                new IngredientComponentStorageCollectionWrapper<>(s1),
                new IngredientComponentStorageCollectionWrapper<>(s2)
        ));
    }

    @Test
    public void testGetComponent() {
        assertThat(storage.getComponent(), is(IngredientComponentStubs.SIMPLE));
    }

    @Test
    public void testIterator() {
        assertThat(Lists.newArrayList(storage.iterator()), is(Lists.newArrayList(5)));

        storage.insert(95, false);
        assertThat(Lists.newArrayList(s1.iterator()), is(Lists.newArrayList(95)));
        assertThat(Lists.newArrayList(s2.iterator()), is(Lists.newArrayList(5)));

        storage.insert(5, false);
        assertThat(Lists.newArrayList(s1.iterator()), is(Lists.newArrayList(100)));
        assertThat(Lists.newArrayList(s2.iterator()), is(Lists.newArrayList(5)));
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
    }

    @Test
    public void testInsert() {
        assertThat(storage.insert(10, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));

        assertThat(storage.insert(10, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 5)));

        assertThat(storage.insert(100, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(10, 5)));

        assertThat(storage.insert(100, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(110, 5)));
    }

    @Test
    public void testExtract() {
        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 5)));

        assertThat(storage.extract(10, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));
    }

    @Test
    public void testExtractMatch() {
        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, true, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 5)));

        assertThat(storage.extract(10, true, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, true, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, true, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, true, true), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, true, false), is(0));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(89, true, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(89, true, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));


        assertThat(storage.insert(100, false), is(0));

        assertThat(storage.extract(10, false, true), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(100, 5)));

        assertThat(storage.extract(10, false, false), is(10));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, false, true), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(90, 5)));

        assertThat(storage.extract(1, false, false), is(1));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, false, true), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(89, 5)));

        assertThat(storage.extract(100, false, false), is(89));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList(5)));

        assertThat(storage.extract(100, false, false), is(5));
        assertThat(Lists.newArrayList(storage), is(Lists.newArrayList()));
    }

}
