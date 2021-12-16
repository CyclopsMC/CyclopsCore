package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class BenchmarkNbtPath {

    public static void main(String[] args) throws NbtParseException {
        // Parsing takes ~2x as much time as execution, so pre-parsing ahead of time is definitely valuable.
        runParse(100000); // 0.00541ms/op
        runExecute(100000); // 0.0038ms/op
    }

    private static void runParse(int replication) {
        benchmark("parse", () -> {
            try {
                NbtPath.parse("$.a.bdef.c[?(@.c == \"B\")]*.x");
            } catch (NbtParseException e) {
                e.printStackTrace();
            }
        }, replication);
    }

    private static void runExecute(int replication) throws NbtParseException {
        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        ListTag tag3 = new ListTag();
        CompoundTag tag4 = new CompoundTag();
        CompoundTag tag5 = new CompoundTag();
        CompoundTag tag6 = new CompoundTag();
        tag1.put("a", tag2);
        tag2.put("b", tag3);
        tag3.add(tag4);
        tag3.add(tag5);
        tag3.add(tag6);
        tag4.putString("notX", "X");
        tag5.putString("x", "X");
        tag6.putString("x", "notX");

        INbtPathExpression expression = NbtPath.parse("$.a.b[?(@.x == \"X\")][0].x");
        benchmark("execute", () -> expression.match(tag1).getMatches().findFirst().get(), replication);
    }

    public static void benchmark(String label, Runnable runnable, int replication) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < replication; i++) {
            runnable.run();
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(label + ": " + ((double) elapsedTime) / replication + "ms/op");
    }

}
