package org.cyclops.cyclopscore.ingredient;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollection;
import org.cyclops.cyclopscore.ingredient.collection.IIngredientCollectionMutable;
import org.cyclops.cyclopscore.ingredient.collection.IngredientArrayList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionMultiClassified;
import org.cyclops.cyclopscore.ingredient.collection.IngredientCollectionSingleClassified;
import org.cyclops.cyclopscore.ingredient.collection.IngredientHashSet;
import org.cyclops.cyclopscore.ingredient.collection.IngredientLinkedList;
import org.cyclops.cyclopscore.ingredient.collection.IngredientTreeSet;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BenchmarkIngredientCollections {

    public static void main(String[] args) {
        List<Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>>> collections = Lists.newArrayList(
                Pair.of("HashSet", new IngredientHashSet<>(IngredientComponentStubs.COMPLEX)),
                Pair.of("TreeSet", new IngredientTreeSet<>(IngredientComponentStubs.COMPLEX)),
                Pair.of("ArrayList", new IngredientArrayList<>(IngredientComponentStubs.COMPLEX)),
                Pair.of("LinkedList", new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX)),

                Pair.of("Classified:HashSet:Group", new IngredientCollectionSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientHashSet(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("Classified:TreeSet:Group", new IngredientCollectionSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientTreeSet(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("Classified:ArrayList:Group", new IngredientCollectionSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientArrayList(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("Classified:LinkedList:Group", new IngredientCollectionSingleClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientLinkedList(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0))),
                Pair.of("MultiClassified:HashSet", new IngredientCollectionMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientHashSet(IngredientComponentStubs.COMPLEX))),
                Pair.of("MultiClassified:TreeSet", new IngredientCollectionMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientTreeSet(IngredientComponentStubs.COMPLEX))),
                Pair.of("MultiClassified:ArrayList", new IngredientCollectionMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientArrayList(IngredientComponentStubs.COMPLEX))),
                Pair.of("MultiClassified:LinkedList", new IngredientCollectionMultiClassified<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientLinkedList(IngredientComponentStubs.COMPLEX)))
        );
        Random rand = new Random(1000);

        runCollections(rand, collections, 1000000, 10000);
    }

    public static void runCollections(Random rand, List<Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>>> collections, int itemCount, int repeatCount) {
        List<ComplexStack> complexStacks = Lists.newArrayList();
        for (int i = 0; i < itemCount; i++) {
            ComplexStack.Group group = ComplexStack.Group.values()[rand.nextInt(ComplexStack.Group.values().length)];
            ComplexStack.Tag tag = ComplexStack.Tag.values()[rand.nextInt(ComplexStack.Tag.values().length)];
            if (rand.nextBoolean()) {
                tag = null;
            }
            ComplexStack complexStack = new ComplexStack(group, rand.nextInt(64) + 1, rand.nextInt(15), tag);
            complexStacks.add(complexStack);
        }

        System.out.println("--- INSERTION ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> pair.getRight().addAll(complexStacks));
        }

        System.out.println("--- ITERATE ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterate(pair.getRight()));
        }

        System.out.println("--- CONTAINS ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> contains(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Group) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP));
        }

        System.out.println("--- CONTAINS MATCH (Group) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP, repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Meta) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META));
        }

        System.out.println("--- CONTAINS MATCH (Meta) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META, repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META));
        }

        System.out.println("--- CONTAINS MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META, repeatCount));
        }
    }

    public static void iterate(IIngredientCollection<ComplexStack, Integer> collection) {
        for (ComplexStack complexStack : collection) {
            // Do nothing
        }
    }

    public static void contains(IIngredientCollection<ComplexStack, Integer> collection, ComplexStack key, int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            collection.contains(key);
        }
    }

    public static void containsMatch(IIngredientCollection<ComplexStack, Integer> collection, ComplexStack key, Integer matchCondition, int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            collection.contains(key, matchCondition);
        }
    }

    public static void iterateMatch(IIngredientCollection<ComplexStack, Integer> collection, ComplexStack key, Integer matchCondition) {
        Iterator<ComplexStack> it = collection.iterator(key, matchCondition);
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
