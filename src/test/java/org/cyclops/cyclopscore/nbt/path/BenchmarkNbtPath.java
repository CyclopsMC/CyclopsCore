package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagList tag3 = new NBTTagList();
        NBTTagCompound tag4 = new NBTTagCompound();
        NBTTagCompound tag5 = new NBTTagCompound();
        NBTTagCompound tag6 = new NBTTagCompound();
        tag1.setTag("a", tag2);
        tag2.setTag("b", tag3);
        tag3.appendTag(tag4);
        tag3.appendTag(tag5);
        tag3.appendTag(tag6);
        tag4.setString("notX", "X");
        tag5.setString("x", "X");
        tag6.setString("x", "notX");

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
