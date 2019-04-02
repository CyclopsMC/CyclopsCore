package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.NBTBase;

import java.util.stream.Stream;

/**
 * An NBT path expression that consists of a list of chained sub-expressions.
 */
public class NbtPathExpressionList implements INbtPathExpression {

    private final INbtPathExpression[] subExpressions;

    public NbtPathExpressionList(INbtPathExpression... subExpressions) {
        this.subExpressions = subExpressions;
    }

    @Override
    public NbtPathExpressionMatches match(Stream<NBTBase> nbt) {
        NbtPathExpressionMatches matches = new NbtPathExpressionMatches(nbt);
        for (INbtPathExpression subExpression : subExpressions) {
            matches = subExpression.match(matches.getMatches());
        }
        return matches;
    }
}
