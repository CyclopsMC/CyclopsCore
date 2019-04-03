package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A handler that picks the parent of the NBT tree in the current execution context.
 */
public class NbtPathExpressionParseHandlerParent implements INbtPathExpressionParseHandler {

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (nbtPathExpression.length() <= pos + 1
                || nbtPathExpression.charAt(pos) != '.' || nbtPathExpression.charAt(pos + 1) != '.') {
            return HandleResult.INVALID;
        }

        return new HandleResult(Expression.INSTANCE, 2);
    }

    public static class Expression implements INbtPathExpression {

        public static final Expression INSTANCE = new Expression();

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(NbtPathExpressionExecutionContext::getParentContext)
                    .filter(Objects::nonNull)
            );
        }
    }
}
