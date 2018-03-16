package org.cyclops.cyclopscore.ingredient;

import javax.annotation.Nullable;

public class ComplexStack {

    private final Group group;
    private final int meta;
    private final int amount;
    @Nullable
    private final Tag tag;

    public ComplexStack(Group group, int meta, int amount, @Nullable Tag tag) {
        this.group = group;
        this.meta = meta;
        this.amount = amount;
        this.tag = tag;
    }

    public Group getGroup() {
        return group;
    }

    public int getMeta() {
        return meta;
    }

    public int getAmount() {
        return amount;
    }

    @Nullable
    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "[C" + getGroup() + ";" + getMeta() + ";" + getAmount() + ";" + getTag() + "]";
    }

    public static enum Group {
        A, B, C, D, E
    }

    public static enum Tag {
        A, B, C, D, E
    }

    public static class Match {

        public static final int ANY = 0;
        public static final int GROUP = 1;
        public static final int META = 2;
        public static final int AMOUNT = 4;
        public static final int TAG = 8;

        public static final int EXACT = GROUP | META | AMOUNT | TAG;

        public static boolean equal(ComplexStack a, ComplexStack b, int matchFlags) {
            if (matchFlags == ANY) {
                return true;
            }
            boolean group = (matchFlags & GROUP) > 0;
            boolean meta = (matchFlags & META) > 0;
            boolean amount = (matchFlags & AMOUNT) > 0;
            boolean tag = (matchFlags & TAG) > 0;
            return a == b || (a != null && b != null
                    && (!group || a.getGroup() == b.getGroup())
                    && (!meta || a.getMeta() == b.getMeta())
                    && (!amount || a.getAmount() == b.getAmount())
                    && (!tag || a.getTag() == b.getTag()));
        }
    }

}
