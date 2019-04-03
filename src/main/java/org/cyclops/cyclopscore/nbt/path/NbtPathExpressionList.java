package org.cyclops.cyclopscore.nbt.path;

import org.cyclops.cyclopscore.nbt.path.parse.NbtPathExpressionExecutionContext;

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
    public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
        NbtPathExpressionMatches matches = new NbtPathExpressionMatches(executionContexts);
        for (INbtPathExpression subExpression : subExpressions) {
            matches = subExpression.matchContexts(matches.getContexts());
        }
        return matches;
    }
}
