package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
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
public class TestIngredientCollectionListComplex {

    private static final ComplexStack CA01_ = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack CB02_ = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack CA91B = new ComplexStack(ComplexStack.Group.A, 9, 1, ComplexStack.Tag.B);
    private static final ComplexStack CA01B = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, 3) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
                { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, new ComplexStack[0]) },
                { new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX) },
                { new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX, Lists.newArrayList()) },
        });
    }

    @Parameterized.Parameter
    public IngredientList<ComplexStack, Integer> collection;

    @Before
    public void beforeEach() {
        collection.clear();
        collection.add(CA01_);
        collection.add(CB02_);
        collection.add(CA91B);
    }

    @Test
    public void testEquals() {
        assertThat(collection.equals(collection), is(true));
        assertThat(collection.equals("abc"), is(false));
        assertThat(collection.equals(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(null), is(false));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B)), is(true));
        assertThat(collection.equals(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE)), is(false));
        assertThat(collection.equals(new IngredientHashSet<>(IngredientComponentStubs.SIMPLE, Lists.newArrayList(0, 1, 2))), is(false));
    }

    @Test
    public void testHashCode() {
        assertThat(collection.hashCode(), is(collection.hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientCollectionEmpty<>(IngredientComponentStubs.SIMPLE).hashCode())));
        assertThat(collection.hashCode(), is(new IngredientArrayList<>(IngredientComponentStubs.COMPLEX, CA01_, CB02_, CA91B).hashCode()));
        assertThat(collection.hashCode(), not(is(new IngredientArrayList<>(IngredientComponentStubs.SIMPLE).hashCode())));
    }

    @Test
    public void testGet() {
        assertThat(collection.get(0), is(CA01_));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
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
        assertThat(collection.set(0, CA01B), is(CA01_));

        assertThat(collection.get(0), is(CA01B));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsSmall() {
        collection.set(-1, CA01B);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsLarge() {
        collection.set(3, CA01B);
    }

    @Test
    public void testAdd() {
        collection.add(0, CA01B);

        assertThat(collection.get(0), is(CA01B));
        assertThat(collection.get(1), is(CA01_));
        assertThat(collection.get(2), is(CB02_));
        assertThat(collection.get(3), is(CA91B));
    }

    @Test
    public void testAddEnd() {
        collection.add(3, CA01B);

        assertThat(collection.get(0), is(CA01_));
        assertThat(collection.get(1), is(CB02_));
        assertThat(collection.get(2), is(CA91B));
        assertThat(collection.get(3), is(CA01B));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsSmall() {
        collection.add(-1, CA01B);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBoundsLarge() {
        collection.add(4, CA01B);
    }

    @Test
    public void testRemove() {
        collection.remove(0);

        assertThat(collection.get(0), is(CB02_));
        assertThat(collection.get(1), is(CA91B));
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
        collection.add(CA01_);

        assertThat(collection.firstIndexOf(CA01_), is(0));

        assertThat(collection.firstIndexOf(CA01B), is(-1));
    }

    @Test
    public void testLastIndexOf() {
        collection.add(CA01_);

        assertThat(collection.lastIndexOf(CA01_), is(3));

        assertThat(collection.firstIndexOf(CA01B), is(-1));
    }

    @Test
    public void testListIterator() {
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

    @Test
    public void testListIteratorOffset() {
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

        assertThat(collection.subList(2, 3), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(CA91B))));

        assertThat(collection.subList(1, 2), is(new IngredientArrayList<>(collection.getComponent(), Lists.newArrayList(CB02_))));
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
        collection.add(CA01B);

        collection.sort(collection.getComponent().getMatcher());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(CA01_, CA01B, CA91B, CB02_)));
    }

    @Test
    public void testSortReverse() {
        collection.sort(collection.getComponent().getMatcher().reversed());

        assertThat(Lists.newArrayList(collection), is(Lists.newArrayList(CB02_, CA91B, CA01_)));
    }

}
