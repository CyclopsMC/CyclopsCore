package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientMapsSimpleParameterized<T, M, V> {

    private static final ComplexStack C0 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack C1 = new ComplexStack(ComplexStack.Group.B, 0, 2, null);
    private static final ComplexStack C2 = new ComplexStack(ComplexStack.Group.A, 10, 1, ComplexStack.Tag.B);
    private static final ComplexStack C3 = new ComplexStack(ComplexStack.Group.A, 0, 1, ComplexStack.Tag.B);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*  0 */ { new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3 },
                /*  1 */ { new IngredientHashMap<>(IngredientComponentStubs.SIMPLE), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3 },
                /*  2 */ { new IngredientMapSingleClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashMap<>(IngredientComponentStubs.SIMPLE),
                IngredientComponentStubs.SIMPLE.getCategoryTypes().get(0)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3 },
                /*  3 */ { new IngredientMapMultiClassified<>(IngredientComponentStubs.SIMPLE,
                () -> new IngredientHashMap<>(IngredientComponentStubs.SIMPLE)), IngredientComponentStubs.SIMPLE, 0, 1, 2, 3, 0, 1, 2, 3 },
        });
    }

    @Parameterized.Parameter(0)
    public IIngredientMapMutable<T, M, V> collection;
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
    @Parameterized.Parameter(6)
    public V va;
    @Parameterized.Parameter(7)
    public V vb;
    @Parameterized.Parameter(8)
    public V vc;
    @Parameterized.Parameter(9)
    public V vd;

    @Before
    public void beforeEach() {
        collection.clear();
    }

    @Test
    public void testContainsMatch() {
        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @Test
    public void testContainsMatchAny() {
        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(b, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(c, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKey(d, collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testContainsAll() {
        assertThat(collection.containsKeyAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d)), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c)), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d)), is(true));
    }

    @Test
    public void testContainsAllMatch() {
        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(true));
    }

    @Test
    public void testContainsAllMatchAny() {
        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d)), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testContainsAllMatchAnyEmpty() {
        assertThat(collection.containsKeyAll(Lists.newArrayList(a), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, c), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));
        assertThat(collection.containsKeyAll(Lists.newArrayList(b, d), collection.getComponent().getMatcher().getAnyMatchCondition()), is(false));

        assertThat(collection.containsKeyAll(Lists.newArrayList(), collection.getComponent().getMatcher().getAnyMatchCondition()), is(true));
    }

    @Test
    public void testIterator() {
        Iterator<Map.Entry<T, V>> it0 = collection.iterator();
        assertThat(it0.hasNext(), is(false));

        assertThat(collection.put(a, va), nullValue());
        Iterator<Map.Entry<T, V>> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(new AbstractMap.SimpleEntry<>(a, va)));
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.put(b, vb), nullValue());
        ArrayList<Map.Entry<T, V>> c2 = Lists.newArrayList(collection.iterator());
        assertThat(c2.size(), is(2));
        assertThat(c2.containsAll(Lists.newArrayList(new AbstractMap.SimpleEntry<>(a, va), new AbstractMap.SimpleEntry<>(b, vb))), is(true));

        assertThat(collection.put(c, vc), nullValue());
        ArrayList<Map.Entry<T, V>> c3 = Lists.newArrayList(collection.iterator());
        assertThat(c3.size(), is(3));
        assertThat(c3.containsAll(Lists.newArrayList(new AbstractMap.SimpleEntry<>(a, va), new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc))), is(true));

        assertThat(collection.put(d, vd), nullValue());
        ArrayList<Map.Entry<T, V>> c4 = Lists.newArrayList(collection.iterator());
        assertThat(c4.size(), is(4));
        assertThat(c4.containsAll(Lists.newArrayList(new AbstractMap.SimpleEntry<>(a, va), new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc), new AbstractMap.SimpleEntry<>(d, vd))), is(true));
    }

}
