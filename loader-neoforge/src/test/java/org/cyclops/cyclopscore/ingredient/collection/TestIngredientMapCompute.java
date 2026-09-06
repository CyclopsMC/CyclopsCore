package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@link IIngredientMapMutable#compute} across the map implementations.
 *
 * It exists so that a read followed by a write can hash the key once instead of twice, so the
 * results have to stay identical to doing both separately.
 *
 * @author rubensworks
 */
@RunWith(Parameterized.class)
public class TestIngredientMapCompute {

    private static final ComplexStack A01 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack A12 = new ComplexStack(ComplexStack.Group.A, 1, 2, null);
    private static final ComplexStack B01 = new ComplexStack(ComplexStack.Group.B, 0, 1, null);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.<Object[]>asList(new Object[][]{
                {(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>>)
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)},
                {(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>>)
                        () -> new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX)},
                {(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>>)
                        () -> new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                                IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))},
        });
    }

    private final Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory;

    public TestIngredientMapCompute(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        this.factory = factory;
    }

    @Test
    public void testInsertsWhenAbsent() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        assertThat(map.compute(A01, (key, value) -> {
            assertThat(value, is(nullValue()));
            return "first";
        }), is("first"));
        assertThat(map.get(A01), is("first"));
        assertThat(map.size(), is(1));
    }

    @Test
    public void testUpdatesWhenPresent() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        map.put(A01, "first");
        assertThat(map.compute(A01, (key, value) -> value + "+second"), is("first+second"));
        assertThat(map.get(A01), is("first+second"));
        assertThat(map.size(), is(1));
    }

    @Test
    public void testRemovesOnNull() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        map.put(A01, "first");
        map.put(A12, "other");
        assertThat(map.compute(A01, (key, value) -> null), is(nullValue()));
        assertThat(map.get(A01), is(nullValue()));
        assertThat(map.get(A12), is("other"));
        assertThat(map.size(), is(1));
    }

    @Test
    public void testNullOnAbsentIsNoOp() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        assertThat(map.compute(A01, (key, value) -> null), is(nullValue()));
        assertThat(map.size(), is(0));
        assertThat(map.isEmpty(), is(true));
    }

    @Test
    public void testKeyIsPassedThrough() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        map.compute(A01, (key, value) -> {
            assertThat(key, is(A01));
            return "v";
        });
    }

    /**
     * The classified map keeps its own size and drops classifiers that run empty, so computing
     * away the last entry of a classifier has to clean up just as a remove would.
     */
    @Test
    public void testSizeTracksAcrossClassifiers() {
        IIngredientMapMutable<ComplexStack, Integer, String> map = this.factory.get();
        map.compute(A01, (key, value) -> "a");
        map.compute(B01, (key, value) -> "b");
        assertThat(map.size(), is(2));
        map.compute(B01, (key, value) -> null);
        assertThat(map.size(), is(1));
        assertThat(map.get(A01), is("a"));
        assertThat(map.get(B01), is(nullValue()));
        map.compute(B01, (key, value) -> "b again");
        assertThat(map.size(), is(2));
        assertThat(map.get(B01), is("b again"));
    }

    @Test
    public void testMatchesGetThenPut() {
        IIngredientMapMutable<ComplexStack, Integer, String> computed = this.factory.get();
        IIngredientMapMutable<ComplexStack, Integer, String> manual = this.factory.get();
        ComplexStack[] keys = {A01, A12, B01, A01, B01, A12};
        for (int i = 0; i < keys.length; i++) {
            ComplexStack key = keys[i];
            int step = i;
            computed.compute(key, (k, value) -> step % 3 == 2 ? null : (value == null ? "v" + step : value + "v" + step));
            String oldValue = manual.get(key);
            String newValue = step % 3 == 2 ? null : (oldValue == null ? "v" + step : oldValue + "v" + step);
            if (newValue == null) {
                manual.remove(key);
            } else {
                manual.put(key, newValue);
            }
            assertThat(computed.size(), is(manual.size()));
            for (ComplexStack probe : new ComplexStack[]{A01, A12, B01}) {
                assertThat(computed.get(probe), is(manual.get(probe)));
            }
        }
    }
}
