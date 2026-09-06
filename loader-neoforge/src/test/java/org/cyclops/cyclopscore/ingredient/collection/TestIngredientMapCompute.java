package org.cyclops.cyclopscore.ingredient.collection;

import org.cyclops.cyclopscore.ingredient.ComplexStack;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestIngredientMapCompute {

    private static final ComplexStack A01 = new ComplexStack(ComplexStack.Group.A, 0, 1, null);
    private static final ComplexStack A12 = new ComplexStack(ComplexStack.Group.A, 1, 2, null);
    private static final ComplexStack B01 = new ComplexStack(ComplexStack.Group.B, 0, 1, null);

    public Stream<Arguments> maps() {
        return Stream.<Supplier<IIngredientMapMutable<ComplexStack, Integer, String>>>of(
                () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                () -> new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX),
                () -> new IngredientMapSingleClassified<>(IngredientComponentStubs.COMPLEX,
                        () -> new IngredientHashMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))
        ).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testInsertsWhenAbsent(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
        assertThat(map.compute(A01, (key, value) -> {
            assertThat(value, is(nullValue()));
            return "first";
        }), is("first"));
        assertThat(map.get(A01), is("first"));
        assertThat(map.size(), is(1));
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testUpdatesWhenPresent(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
        map.put(A01, "first");
        assertThat(map.compute(A01, (key, value) -> value + "+second"), is("first+second"));
        assertThat(map.get(A01), is("first+second"));
        assertThat(map.size(), is(1));
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testRemovesOnNull(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
        map.put(A01, "first");
        map.put(A12, "other");
        assertThat(map.compute(A01, (key, value) -> null), is(nullValue()));
        assertThat(map.get(A01), is(nullValue()));
        assertThat(map.get(A12), is("other"));
        assertThat(map.size(), is(1));
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testNullOnAbsentIsNoOp(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
        assertThat(map.compute(A01, (key, value) -> null), is(nullValue()));
        assertThat(map.size(), is(0));
        assertThat(map.isEmpty(), is(true));
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testKeyIsPassedThrough(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
        map.compute(A01, (key, value) -> {
            assertThat(key, is(A01));
            return "v";
        });
    }

    /**
     * The classified map keeps its own size and drops classifiers that run empty, so computing
     * away the last entry of a classifier has to clean up just as a remove would.
     */
    @ParameterizedTest
    @MethodSource("maps")
    public void testSizeTracksAcrossClassifiers(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> map = factory.get();
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

    @ParameterizedTest
    @MethodSource("maps")
    public void testMatchesGetThenPut(Supplier<IIngredientMapMutable<ComplexStack, Integer, String>> factory) {
        IIngredientMapMutable<ComplexStack, Integer, String> computed = factory.get();
        IIngredientMapMutable<ComplexStack, Integer, String> manual = factory.get();
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
