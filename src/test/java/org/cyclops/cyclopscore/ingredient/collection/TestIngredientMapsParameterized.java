package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TestIngredientMapsParameterized<T, M, V> {

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

                /*  3 */ { new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
                /*  4 */ { new IngredientHashMap<>(IngredientComponentStubs.COMPLEX), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
                /*  5 */ { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
                /*  6 */ { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(1)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
                /*  7 */ { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(2)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
                /*  8 */ { new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(3)), IngredientComponentStubs.COMPLEX, C0, C1, C2, C3, 0, 1, 2, 3 },
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
    public void testGetComponent() {
        assertThat(collection.getComponent(), is(component));
    }

    @Test
    public void testEmpty() {
        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));

        assertThat(collection.toString(), equalTo("[]"));
        assertThat(collection.hashCode(), is(IngredientCollections.emptyCollection(collection.getComponent()).hashCode()));
    }

    @Test
    public void testPutSingle() {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(1));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testRemoveSingle() {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.remove(b), is(vb));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testRemoveRepeatedSingle() {
        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testPutRemoveCycleSingle() {
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(1));

        assertThat(collection.remove(b), is(vb));
        assertThat(collection.remove(b), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testPutMultiple() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testPutRepeatedKey() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.get(a), is(va));
        assertThat(collection.put(a, vb), is(va));
        assertThat(collection.get(a), is(vb));
        assertThat(collection.put(a, vc), is(vb));
        assertThat(collection.get(a), is(vc));
    }

    @Test
    public void testPutAll() {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(this.component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection, not(equalTo(IngredientCollections.emptyCollection(collection.getComponent()))));
    }

    @Test
    public void testGetAll() {
        collection.put(a, va);
        collection.put(b, vb);
        collection.put(c, vc);

        assertThat(Sets.newHashSet(collection.getAll(a, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(va)));
        assertThat(Sets.newHashSet(collection.getAll(b, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(vb)));
        assertThat(Sets.newHashSet(collection.getAll(c, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet(vc)));
        assertThat(Sets.newHashSet(collection.getAll(d, collection.getComponent().getMatcher().getExactMatchCondition())), is(Sets.newHashSet()));

        assertThat(Sets.newHashSet(collection.getAll(a, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(b, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(c, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
        assertThat(Sets.newHashSet(collection.getAll(d, collection.getComponent().getMatcher().getAnyMatchCondition())), is(Sets.newHashSet(va, vb, vc)));
    }

    @Test
    public void testRemoveMultiple() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAllMultiple() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection.removeAll(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(2));
        assertThat(collection.removeAll(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(1));
        assertThat(collection.removeAll(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.size(), is(0));
        assertThat(collection.removeAll(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAll() {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(this.component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d)), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testRemoveAllMatch() {
        IngredientHashMap<T, M, V> subIngredientMap = new IngredientHashMap<>(this.component);
        subIngredientMap.put(a, va);
        subIngredientMap.put(b, vb);
        subIngredientMap.put(c, vc);
        assertThat(collection.putAll(subIngredientMap), is(3));
        assertThat(collection.size(), is(3));

        assertThat(collection.removeAll(Lists.newArrayList(a, b, c, d), collection.getComponent().getMatcher().getExactMatchCondition()), is(3));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testRemoveRepeatedMultiple() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
    }

    @Test
    public void testPutRemoveCycleMultiple() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));
        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.size(), is(3));

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(1));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(3));

        assertThat(collection.remove(a), is(va));
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(a), nullValue());
        assertThat(collection.size(), is(2));
        assertThat(collection.remove(b), is(vb));
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(b), nullValue());
        assertThat(collection.size(), is(1));
        assertThat(collection.remove(c), is(vc));
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(c), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));
        assertThat(collection.remove(d), nullValue());
        assertThat(collection.size(), is(0));

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(b, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(c, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(d, collection.getComponent().getMatcher().getExactMatchCondition()), is(0));
        assertThat(collection.countKey(a, collection.getComponent().getMatcher().getAnyMatchCondition()), is(0));
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
        assertThat(collection.put(b, vb), nullValue());
        assertThat(collection.put(c, vc), nullValue());
        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.isEmpty(), is(false));
        assertThat(collection.size(), is(3));

        collection.clear();

        assertThat(collection.isEmpty(), is(true));
        assertThat(collection.size(), is(0));
    }

    @Test
    public void testContainsKey() {
        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(false));
        assertThat(collection.containsKey(c), is(false));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(false));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(true));
        assertThat(collection.containsKey(d), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsKey(a), is(false));
        assertThat(collection.containsKey(b), is(true));
        assertThat(collection.containsKey(c), is(true));
        assertThat(collection.containsKey(d), is(true));
    }

    @Test
    public void testContainsValue() {
        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(false));
        assertThat(collection.containsValue(vc), is(false));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(false));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(true));
        assertThat(collection.containsValue(vd), is(false));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.containsValue(va), is(false));
        assertThat(collection.containsValue(vb), is(true));
        assertThat(collection.containsValue(vc), is(true));
        assertThat(collection.containsValue(vd), is(true));
    }

    @Test
    public void testKeySet() {
        assertThat(collection.keySet(), is(new IngredientHashSet<>(this.component)));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(this.component, Lists.newArrayList(b))));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(this.component, Lists.newArrayList(b, c))));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.keySet(), is(new IngredientHashSet<>(this.component, Lists.newArrayList(b, c, d))));
    }

    @Test
    public void testValues() {
        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet()));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb)));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb, vc)));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(Sets.newHashSet(collection.values()), is(Sets.newHashSet(vb, vc, vd)));
    }

    @Test
    public void testEntrySet() {
        assertThat(collection.entrySet(), is(Sets.newHashSet()));

        assertThat(collection.put(b, vb), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb))));

        assertThat(collection.put(c, vc), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc))));

        assertThat(collection.put(d, vd), nullValue());

        assertThat(collection.entrySet(), is(Sets.newHashSet(new AbstractMap.SimpleEntry<>(b, vb), new AbstractMap.SimpleEntry<>(c, vc), new AbstractMap.SimpleEntry<>(d, vd))));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorEmptyCollectionNext() {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        it.next();
    }

    @Test
    public void testIteratorRemove() {
        assertThat(collection.put(a, va), nullValue());
        assertThat(collection.size(), is(1));

        Iterator<Map.Entry<T, V>> it1 = collection.iterator();
        assertThat(it1.hasNext(), is(true));
        assertThat(it1.next(), is(new AbstractMap.SimpleEntry<>(a, va)));
        it1.remove();
        assertThat(it1.hasNext(), is(false));

        assertThat(collection.size(), is(0));
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveBeforeStart() {
        assertThat(collection.put(a, va), nullValue());
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveBeforeStartEmpty() {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        it.remove();
    }

    @Test(expected = RuntimeException.class)
    public void testIteratorRemoveMultiple() {
        Iterator<Map.Entry<T, V>> it = collection.iterator();
        it.remove();
        it.remove();
        it.remove();
    }

}
