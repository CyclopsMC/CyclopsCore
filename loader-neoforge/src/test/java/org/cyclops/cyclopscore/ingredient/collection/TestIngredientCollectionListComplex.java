package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ListIterator;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionListComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, 3),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()),
                new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, new ComplexStack[0]),
                new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX),
                new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList())
                ).map(collection -> {
            collection.clear();
            collection.add(CA01_);
            collection.add(CB02_);
            collection.add(CA91B);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(IngredientList<ComplexStack, Integer> collection) {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IngredientList<ComplexStack, Integer> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGet(IngredientList<ComplexStack, Integer> collection) {
        assertThat(collection.get(0), is(CA01_));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->collection.get(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.get(3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSet(IngredientList<ComplexStack, Integer> collection) {
        assertThat(collection.set(0, CA01B), is(CA01_));

        assertThat(collection.get(0), is(CA01B));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSetOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.set(-1, CA01B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSetOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.set(3, CA01B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAdd(IngredientList<ComplexStack, Integer> collection) {
        collection.add(0, CA01B);

        assertThat(collection.get(0), is(CA01B));
        assertThat(collection.get(1), is(CA01_));
        assertThat(collection.get(2), is(CB02_));
        assertThat(collection.get(3), is(CA91B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddEnd(IngredientList<ComplexStack, Integer> collection) {
        collection.add(3, CA01B);

        assertThat(collection.get(0), is(CA01_));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
        assertThat(collection.get(3), is(CA01B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.add(-1, CA01B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () ->collection.add(4, CA01B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemove(IngredientList<ComplexStack, Integer> collection) {
        collection.remove(0);

        assertThat(collection.get(0), is(CB02_));
        assertThat(collection.get(1), is(CA91B));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testFirstIndexOf(IngredientList<ComplexStack, Integer> collection) {
        collection.add(CA01_);

        assertThat(collection.firstIndexOf(CA01_), is(0));

        assertThat(collection.firstIndexOf(CA01B), is(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testLastIndexOf(IngredientList<ComplexStack, Integer> collection) {
        collection.add(CA01_);

        assertThat(collection.lastIndexOf(CA01_), is(3));

        assertThat(collection.firstIndexOf(CA01B), is(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIterator(IngredientList<ComplexStack, Integer> collection) {
        ListIterator<ComplexStack> it = collection.listIterator();

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
        assertThat(it.nextIndex(), is(0));
        assertThat(it.next(), is(CA01_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(1));
        assertThat(it.next(), is(CB02_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(2));
        assertThat(it.next(), is(CA91B));

        assertThat(it.hasNext(), is(false));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(2));
        assertThat(it.previous(), is(CA91B));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(1));
        assertThat(it.previous(), is(CB02_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(0));
        assertThat(it.previous(), is(CA01_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOffset(IngredientList<ComplexStack, Integer> collection) {
        ListIterator<ComplexStack> it = collection.listIterator(1);

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(1));
        assertThat(it.next(), is(CB02_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(2));
        assertThat(it.next(), is(CA91B));

        assertThat(it.hasNext(), is(false));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(2));
        assertThat(it.previous(), is(CA91B));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(1));
        assertThat(it.previous(), is(CB02_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(0));
        assertThat(it.previous(), is(CA01_));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.listIterator(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.listIterator(4));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubList(IngredientList<ComplexStack, Integer> collection) {
        assertThat(collection.subList(0, 3), is(collection));

        assertThat(collection.subList(2, 3), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(CA91B))));

        assertThat(collection.subList(1, 2), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(CB02_))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsSmall(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.subList(-1, 3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsLarge(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.subList(0, 4));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsRange(IngredientList<ComplexStack, Integer> collection) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> collection.subList(1, 0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSort(IngredientList<ComplexStack, Integer> collection) {
        collection.add(CA01B);

        collection.sort(collection.getComponent().getMatcher());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(CA01_, CA01B, CA91B, CB02_)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSortReverse(IngredientList<ComplexStack, Integer> collection) {
        collection.sort(collection.getComponent().getMatcher().reversed());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(CB02_, CA91B, CA01_)));
    }

}
