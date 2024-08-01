package org.cyclops.cyclopscore.ingredient;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.ingredient.collection.*;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BenchmarkIngredientCollectionsCollapsed {

    /*
    Main findings:
    - Both collections perform similarly for insertion.
    - Contains-match and iterate-match is slightly slower for classified when no category type matches.
    - Contains-match and iterate-match is significantly faster for classified when a category type matches.
    => Overall classified is better for match-based contains or iteration.
       If only exact lookups are required, then prototyped might be better.
       If needs are unknown, then defaulting to the classified collection is a good strategy.
     */

    public static void main(String[] args) {
        List<Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>>> collections = Lists.newArrayList(
                Pair.of("IngredientCollectionPrototypeMap", new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.COMPLEX)),
                Pair.of("Classified:PrototypeMap:Group", new IngredientCollectionSingleClassifiedCollapsed<>(
                        IngredientComponentStubs.COMPLEX, () -> new IngredientCollectionPrototypeMap<>(IngredientComponentStubs.COMPLEX),
                        IngredientComponentStubs.COMPLEX.getCategoryTypes().get(0)))

                // Disabled, as their impl is incomplete
                /*Pair.of("QuantitativeGrouper:ArrayList", new IngredientCollectionQuantitativeGrouper<>(
                        new IngredientArrayList<>(IngredientComponentStubs.COMPLEX))),
                Pair.of("QuantitativeGrouper:LinkedList", new IngredientCollectionQuantitativeGrouper<>(
                        new IngredientLinkedList<>(IngredientComponentStubs.COMPLEX))),*/
        );
        Random rand = new Random(1000);

        runCollections(rand, collections, 1000000, 100000);
    }

    public static void runCollections(Random rand, List<Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>>> collections, int itemCount, int repeatCount) {
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
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> pair.getRight().addAll(complexStacks));
        }

        System.out.println("--- ITERATE ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterate(pair.getRight(), repeatCount));
        }

        System.out.println("--- CONTAINS ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> contains(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Group) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP, repeatCount));
        }

        System.out.println("--- CONTAINS MATCH (Group) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP, repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Meta) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META, repeatCount));
        }

        System.out.println("--- CONTAINS MATCH (Meta) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.META, repeatCount));
        }

        System.out.println("--- ITERATE MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> iterateMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META, repeatCount));
        }

        System.out.println("--- CONTAINS MATCH (Group + Meta) ---");
        for (Pair<String, IIngredientCollapsedCollectionMutable<ComplexStack, Integer>> pair : collections) {
            benchmark(pair.getLeft(), () -> containsMatch(pair.getRight(), new ComplexStack(ComplexStack.Group.B, 10, 10, null), ComplexStack.Match.GROUP | ComplexStack.Match.META, repeatCount));
        }
    }

    public static void iterate(IIngredientCollection<ComplexStack, Integer> collection, int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            for (ComplexStack complexStack : collection) {
                complexStack.hashCode();
            }
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

    public static void iterateMatch(IIngredientCollection<ComplexStack, Integer> collection, ComplexStack key, Integer matchCondition, int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            Iterator<ComplexStack> it = collection.iterator(key, matchCondition);
            while (it.hasNext()) {
                ComplexStack complexStack = it.next();
                complexStack.hashCode();
            }
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
