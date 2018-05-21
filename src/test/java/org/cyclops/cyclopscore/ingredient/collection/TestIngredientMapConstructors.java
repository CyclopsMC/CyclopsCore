package org.cyclops.cyclopscore.ingredient.collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.cyclops.commoncapabilities.api.ingredient.IngredientInstanceWrapper;
import org.cyclops.cyclopscore.ingredient.IngredientComponentStubs;
import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestIngredientMapConstructors {

    @Test
    public void testHashMap() {
        IngredientHashMap<Integer, Boolean, Integer> map0 = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE);
        IngredientHashMap<Integer, Boolean, Integer> map1 = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, 10);
        IngredientTreeMap<Integer, Boolean, Integer> subIngredientMap = new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE);
        subIngredientMap.put(0, 0);
        subIngredientMap.put(1, 10);
        subIngredientMap.put(2, 20);
        IngredientHashMap<Integer, Boolean, Integer> map2 = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, subIngredientMap);
        HashMap<IngredientInstanceWrapper<Integer, Boolean>, Integer> subMap = Maps.newHashMap();
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 0), 0);
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 1), 10);
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 2), 20);
        IngredientHashMap<Integer, Boolean, Integer> map3 = new IngredientHashMap<>(IngredientComponentStubs.SIMPLE, subMap);
        assertThat(map0.isEmpty(), is(true));
        assertThat(map1.isEmpty(), is(true));
        assertThat(map2.containsKeyAll(Lists.newArrayList(0, 1, 2)), is(true));
        assertThat(map3.containsKeyAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

    @Test
    public void testTreeMap() {
        IngredientTreeMap<Integer, Boolean, Integer> map0 = new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE);
        IngredientTreeMap<Integer, Boolean, Integer> subIngredientMap = new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE);
        subIngredientMap.put(0, 0);
        subIngredientMap.put(1, 10);
        subIngredientMap.put(2, 20);
        IngredientTreeMap<Integer, Boolean, Integer> map1 = new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, subIngredientMap);
        TreeMap<IngredientInstanceWrapper<Integer, Boolean>, Integer> subMap = Maps.newTreeMap();
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 0), 0);
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 1), 10);
        subMap.put(new IngredientInstanceWrapper<>(IngredientComponentStubs.SIMPLE, 2), 20);
        IngredientTreeMap<Integer, Boolean, Integer> map2 = new IngredientTreeMap<>(IngredientComponentStubs.SIMPLE, subMap);
        assertThat(map0.isEmpty(), is(true));
        assertThat(map1.containsKeyAll(Lists.newArrayList(0, 1, 2)), is(true));
        assertThat(map2.containsKeyAll(Lists.newArrayList(0, 1, 2)), is(true));
    }

}
