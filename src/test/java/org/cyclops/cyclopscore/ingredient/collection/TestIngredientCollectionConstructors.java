package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientCollectionConstructors {

    @Test
    public void testArrayList() {
        IngredientArrayList<Integer, Boolean> list0 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE);
        IngredientArrayList<Integer, Boolean> list1 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 10);
        IngredientArrayList<Integer, Boolean> list2 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientArrayList<Integer, Boolean> list3 = new IngredientArrayList<>(IngredientComponentStubs.SIMPLE,0, 1, 2);
        assertThat(list0.isEmpty(), is(true));
        assertThat(list1.isEmpty(), is(true));
        assertThat(list2.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
        assertThat(list3.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

    @Test
    public void testLinkedList() {
        IngredientLinkedList<Integer, Boolean> list0 = new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE);
        IngredientLinkedList<Integer, Boolean> list1 = new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        assertThat(list0.isEmpty(), is(true));
        assertThat(list1.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

    @Test
    public void testHashSet() {
        IngredientHashSet<Integer, Boolean> list0 = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE);
        IngredientHashSet<Integer, Boolean> list1 = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, 10);
        IngredientHashSet<Integer, Boolean> list2 = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        IngredientHashSet<Integer, Boolean> list3 = new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Iterables.concat(Lists.newArrayList(0, 1, 2)));
        assertThat(list0.isEmpty(), is(true));
        assertThat(list1.isEmpty(), is(true));
        assertThat(list2.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
        assertThat(list3.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

    @Test
    public void testTreeSet() {
        IngredientTreeSet<Integer, Boolean> list0 = new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE);
        IngredientTreeSet<Integer, Boolean> list1 = new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2));
        assertThat(list0.isEmpty(), is(true));
        assertThat(list1.containsAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

}
