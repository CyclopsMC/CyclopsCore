package org.cyclops.cyclopscore.ingredient;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientMapMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientHashMap;
import org.cyclops.cyclopscore.ingredient.collection.IngredientMapMultiClassified;
import org.cyclops.cyclopscore.ingredient.collection.IngredientMapSingleClassified;
import org.cyclops.cyclopscore.ingredient.collection.IngredientTreeMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BenchmarkIngredientMaps {

    public static void main(String[] args) {
        List<Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>>> collections = Lists.newArrayList(
                Pair.of("HashMap", new IngredientHashMap<>(IngredientComponentStubs.COMPLEX)),
                Pair.of("TreeMap", new IngredientTreeMap<>(IngredientComponentStubs.COMPLEX)),

                Pair.of("Classified:HashMap:Group", new IngredientMapSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientHashMap(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("Classified:TreeMap:Group", new IngredientMapSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientTreeMap(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("MultiClassified:HashMap:Group", new IngredientMapMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientHashMap(IngredientComponentStubs.COMPLEX))),
                Pair.of("MultiClassified:TreeMap:Group", new IngredientMapMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientTreeMap(IngredientComponentStubs.COMPLEX)))
        );
        Random rand = new Random(1000);

        runCollections(rand, collections, 1000000, 100000);
    }

    public static void runCollections(Random rand, List<Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>>> collections, int itemCount, int repeatCount) {
        IIngredientMapMutable<ComplexStack, Integer, Integer> complexStacks = new IngredientHashMap<>(IngredientComponentStubs.COMPLEX);
        for (int i = 0; i < itemCount; i++) {
            ComplexStack.Group group = ComplexStack.Group.values()[rand.nextInt(ComplexStack.Group.values().length)];
            ComplexStack.Tag tag = ComplexStack.Tag.values()[rand.nextInt(ComplexStack.Tag.values().length)];
            if (rand.nextBoolean()) {
                tag = null;
            }
            ComplexStack complexStack = new ComplexStack(group, rand.nextInt(64) + 1, rand.nextInt(15), tag);
            complexStacks.put(complexStack, rand.nextInt());
        }

        System.out.println("--- INSERTION ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> pair.getRight().putAll(complexStacks));
        }

        System.out.println("--- ITERATE ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterate(pair.getRight()));
        }

        System.out.println("--- GET ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> get(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Group) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP));
        }

        System.out.println("--- CONTAINS MATCH (Group) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP));
        }

        System.out.println("--- ITERATE MATCH (Meta) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META));
        }

        System.out.println("--- CONTAINS MATCH (Meta) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META));
        }

        System.out.println("--- ITERATE MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META));
        }

        System.out.println("--- CONTAINS MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientMapMutable<ComplexStack, Integer, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META));
        }
    }

    public static void iterate(IIngredientMapMutable<ComplexStack, Integer, Integer> collection) {
        for (Map.Entry<ComplexStack, Integer> entry : collection) {
            // Do nothing
        }
    }

    public static void get(IIngredientMapMutable<ComplexStack, Integer, Integer> collection, ComplexStack key, int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            collection.get(key);
        }
    }

    public static void containsMatch(IIngredientMapMutable<ComplexStack, Integer, Integer> collection, ComplexStack key, Integer matchCondition) {
        collection.containsKey(key, matchCondition);
    }

    public static void iterateMatch(IIngredientMapMutable<ComplexStack, Integer, Integer> collection, ComplexStack key, Integer matchCondition) {
        Iterator<Map.Entry<ComplexStack, Integer>> it = collection.iterator(key, matchCondition);
        while (it.hasNext()) {
            it.next();
        }
    }

    public static void benchmark(String label, Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(label + ": " + elapsedTime);
    }

}
