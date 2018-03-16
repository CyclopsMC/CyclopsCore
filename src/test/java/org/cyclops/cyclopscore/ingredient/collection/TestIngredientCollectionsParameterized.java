package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientCollectionsParameterized<T, M> {

    private static final ComplexStack C0 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack C1 = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack C2 = new ComplexStack(ComplexStack.Group.A, 10, 1, ComplexStack.Tag.B);
    private static final ComplexStack C3 = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*  0 */ { new IngredientArrayList<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3 },
                /*  1 */ { new IngredientLinkedList<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3 },
                /*  2 */ { new IngredientTreeSet<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3 },
                /*  3 */ { new IngredientHashSet<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3 },
                /*  4 */ { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashSet<>(IngredientComponentStubs.SIMPLE),
                IngredientComponentStubs.SIMPLE.getCategoryTypes().get(0)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3 },

                /*  5 */ { new IngredientArrayList<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /*  6 */ { new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /*  7 */ { new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /*  8 */ { new IngredientHashSet<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /*  9 */ { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /* 10 */ { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /* 11 */ { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
                /* 12 */ { new IngredientCollectionSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashSet<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3 },
        });
    }

    @Parameterized.Parameter(0)
    public IIngredientCollectionMutable<T, M> collection;
    @Parameterized.Parameter(1)
    public IngredientComponent<T, M> component;
    @Parameterized.Parameter(2)
    public T a;
    @Parameterized.Parameter(3)
    public T b;
    @Parameterized.Parameter(4)
    public T c;
    @Parameterized.Parameter(5)
    public T d;

    @Before
    public void beforeEach() {
        collection.clear();
    }

    @Test
    public void testGetComponent() {
        assertThat(collection.getComponent(), is(component));
    }

    @Test
    public void testEmpty() {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.toString(), equalTo("[]"));
        assertThat(collection.hashCode(), is(IngredientCollections.emptyCollection(collection.getComponent()).hashCode()));
    }

    @Test
    public void testAddSingle() {
        assertThat(collection.add(b), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testRemoveSingle() {
        assertThat(collection.add(b), is(true));

        assertThat(collection.remove(b), is(true));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testRemoveRepeatedSingle() {
        assertThat(collection.add(b), is(true));

        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testAddRemoveCycleSingle() {
        assertThat(collection.add(b), is(true));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.add(b), is(true));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));

        assertThat(collection.remove(b), is(true));
        assertThat(collection.remove(b), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testAddMultiple() {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testAddAll() {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testRemoveMultiple() {
        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAllMultiple() {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.removeAll(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAll() {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d)), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAllMatch() {
        assertThat(collection.addAll(Lists.newArrayList(a, b, c)), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testRemoveRepeatedMultiple() {
        assertThat(collection.add(a), is(true));
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testAddRemoveCycleMultiple() {
        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.add(a), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.add(b), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.add(c), is(true));
        assertThat(collection.size(), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.remove(a), is(true));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), is(false));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(true));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), is(false));
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.count(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.count(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
    }

    @Test
    public void testClearEmpty() {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @Test
    public void testClearNonEmpty() {
        assertThat(collection.add(b), is(true));
        assertThat(collection.add(c), is(true));
        assertThat(collection.add(d), is(true));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @Test
    public void testContains() {
        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(false));
        assertThat(collection.contains(c), is(false));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(false));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(true));
        assertThat(collection.contains(d), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a), is(false));
        assertThat(collection.contains(b), is(true));
        assertThat(collection.contains(c), is(true));
        assertThat(collection.contains(d), is(true));
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @Test
    public void testContainsMatchAny() {
        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(c), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(d), is(true));

        assertThat(collection.contains(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.contains(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testContainsAll() {
        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d)), is(true));
    }

    @Test
    public void testContainsAllMatch() {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @Test
    public void testContainsAllMatchAny() {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.add(b), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(c), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.add(d), is(true));

        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testContainsAllMatchAnyEmpty() {
        assertThat(collection.containsAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.containsAll(Lists.newArrayList(), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testIterator() {
        Iterator<T> it0 = collection.iterator();
        assertThat(it0.hasNext(), is(false));

        assertThat(collection.add(a), is(true));
        Iterator<T> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(a));
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.add(b), is(true));
        ArrayList<T> c2 = Lists.newArrayList(collection.iterator());
        assertThat(c2.size(), is(2));
        assertThat(c2.containsAll(Lists.newArrayList(a, b)), is(true));

        assertThat(collection.add(c), is(true));
        ArrayList<T> c3 = Lists.newArrayList(collection.iterator());
        assertThat(c3.size(), is(3));
        assertThat(c3.containsAll(Lists.newArrayList(a, b, c)), is(true));

        assertThat(collection.add(d), is(true));
        ArrayList<T> c4 = Lists.newArrayList(collection.iterator());
        assertThat(c4.size(), is(4));
        assertThat(c4.containsAll(Lists.newArrayList(a, b, c, d)), is(true));
    }

}
