package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionListSimple {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 3) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, new Integer[0]) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE) },
                { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IngredientList<Integer, Boolean> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(0);
        collection.add(1);
        collection.add(2);
    }

    @Test
    public void testEquals() {
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

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.COMPLEX).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE, 0, 1, 2).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX).hashCode())));
    }

    @Test
    public void testGet() {
        assertThat(collection.get(0), is(0));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsSmall() {
        collection.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsLarge() {
        collection.get(3);
    }

    @Test
    public void testSet() {
        assertThat(collection.set(0, 10), is(0));

        assertThat(collection.get(0), is(10));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsSmall() {
        collection.set(-1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsLarge() {
        collection.set(3, 10);
    }

    @Test
    public void testAdd() {
        collection.add(0, 10);

        assertThat(collection.get(0), is(10));
        assertThat(collection.get(1), is(0));
        assertThat(collection.get(2), is(1));
        assertThat(collection.get(3), is(2));
    }

    @Test
    public void testAddEnd() {
        collection.add(3, 10);

        assertThat(collection.get(0), is(0));
        assertThat(collection.get(1), is(1));
        assertThat(collection.get(2), is(2));
        assertThat(collection.get(3), is(10));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsSmall() {
        collection.add(-1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsLarge() {
        collection.add(4, 10);
    }

    @Test
    public void testRemove() {
        collection.remove(0);

        assertThat(collection.get(0), is(1));
        assertThat(collection.get(1), is(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBoundsSmall() {
        collection.remove(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveOutOfBoundsLarge() {
        collection.remove(3);
    }

    @Test
    public void testFirstIndexOf() {
        collection.add(0);

        assertThat(collection.firstIndexOf(0), is(0));

        assertThat(collection.firstIndexOf(10), is(-1));
    }

    @Test
    public void testLastIndexOf() {
        collection.add(0);

        assertThat(collection.lastIndexOf(0), is(3));

        assertThat(collection.firstIndexOf(10), is(-1));
    }

    @Test
    public void testListIterator() {
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

    @Test
    public void testListIteratorOffset() {
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

    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIteratorOutOfBoundsSmall() {
        collection.listIterator(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIteratorOutOfBoundsLarge() {
        collection.listIterator(4);
    }

    @Test
    public void testSubList() {
        assertThat(collection.subList(0, 3), is(collection));

        assertThat(collection.subList(2, 3), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(2))));

        assertThat(collection.subList(1, 2), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(1))));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListOutOfBoundsSmall() {
        collection.subList(-1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSubListOutOfBoundsLarge() {
        collection.subList(0, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubListOutOfBoundsRange() {
        collection.subList(1, 0);
    }

    @Test
    public void testSort() {
        collection.add(-10);

        collection.sort(Comparator.naturalOrder());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(-10, 0, 1, 2)));
    }

    @Test
    public void testSortReverse() {
        collection.sort(Comparator.reverseOrder());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(2, 1, 0)));
    }
}
