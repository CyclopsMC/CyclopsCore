package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.ListIterator;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientCollectionListSimple {

    public Stream<Arguments> data() {
        return Stream.of(
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()),
                new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, new Integer[0]),
                new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE),
                new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList())
                ).map(collection -> {
            collection.clear();
            collection.add(0);
            collection.add(1);
            collection.add(2);
            return collection;
        }).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testEquals(IngredientList<Integer, Boolean> collection) {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 3)), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1)), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testHashCode(IngredientList<Integer, Boolean> collection) {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGet(IngredientList<Integer, Boolean> collection) {
        assertThat(collection.get(0), is(0));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.get(3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSet(IngredientList<Integer, Boolean> collection) {
        assertThat(collection.set(0, 10), is(0));

        assertThat(collection.get(0), is(10));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSetOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.set(-1, 10));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSetOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.set(3, 10));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAdd(IngredientList<Integer, Boolean> collection) {
        collection.add(0, 10);

        assertThat(collection.get(0), is(10));
        assertThat(collection.get(1), is(0));
        assertThat(collection.get(2), is(1));
        assertThat(collection.get(3), is(2));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddEnd(IngredientList<Integer, Boolean> collection) {
        collection.add(3, 10);

        assertThat(collection.get(0), is(0));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
        assertThat(collection.get(3), is(10));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.add(-1, 10));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testAddOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.add(4, 10));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemove(IngredientList<Integer, Boolean> collection) {
        collection.remove(0);

        assertThat(collection.get(0), is(1));
        assertThat(collection.get(1), is(2));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testRemoveOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testFirstIndexOf(IngredientList<Integer, Boolean> collection) {
        collection.add(0);

        assertThat(collection.firstIndexOf(0), is(0));

        assertThat(collection.firstIndexOf(10), is(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testLastIndexOf(IngredientList<Integer, Boolean> collection) {
        collection.add(0);

        assertThat(collection.lastIndexOf(0), is(3));

        assertThat(collection.firstIndexOf(10), is(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIterator(IngredientList<Integer, Boolean> collection) {
        ListIterator<Integer> it = collection.listIterator();

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
        assertThat(it.nextIndex(), is(0));
        assertThat(it.next(), is(0));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(1));
        assertThat(it.next(), is(1));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(2));
        assertThat(it.next(), is(2));

        assertThat(it.hasNext(), is(false));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(2));
        assertThat(it.previous(), is(2));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(1));
        assertThat(it.previous(), is(1));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(0));
        assertThat(it.previous(), is(0));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOffset(IngredientList<Integer, Boolean> collection) {
        ListIterator<Integer> it = collection.listIterator(1);

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(1));
        assertThat(it.next(), is(1));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.nextIndex(), is(2));
        assertThat(it.next(), is(2));

        assertThat(it.hasNext(), is(false));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(2));
        assertThat(it.previous(), is(2));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(1));
        assertThat(it.previous(), is(1));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(true));
        assertThat(it.previousIndex(), is(0));
        assertThat(it.previous(), is(0));

        assertThat(it.hasNext(), is(true));
        assertThat(it.hasPrevious(), is(false));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.listIterator(-1));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testListIteratorOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.listIterator(4));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubList(IngredientList<Integer, Boolean> collection) {
        assertThat(collection.subList(0, 3), is(collection));

        assertThat(collection.subList(2, 3), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(2))));

        assertThat(collection.subList(1, 2), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(1))));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsSmall(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.subList(-1, 3));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsLarge(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> collection.subList(0, 4));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSubListOutOfBoundsRange(IngredientList<Integer, Boolean> collection) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> collection.subList(1, 0));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSort(IngredientList<Integer, Boolean> collection) {
        collection.add(-10);

        collection.sort(Comparator.naturalOrder());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(-10, 0, 1, 2)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testSortReverse(IngredientList<Integer, Boolean> collection) {
        collection.sort(Comparator.reverseOrder());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(2, 1, 0)));
    }
}
