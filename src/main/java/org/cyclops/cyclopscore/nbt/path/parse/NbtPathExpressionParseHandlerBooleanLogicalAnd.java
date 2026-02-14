package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles boolean AND expressions in the form of "expression1 {@literal &&} expression2".
 */
public class NbtPathExpressionParseHandlerBooleanLogicalAnd implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_EXPRESSION = Pattern.compile("^ *&& *");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_EXPRESSION
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        // Parse the right-hand side expression
        int rightPos = pos + matcher.group().length();
        if (rightPos >= nbtPathExpression.length()) {
            return HandleResult.INVALID;
        }

        // Find the end of the right expression
        int endPos = NbtPathExpressionHelpers.findExpressionEnd(nbtPathExpression, rightPos);
        if (endPos == rightPos) {
            return HandleResult.INVALID;
        }

        String rightExpressionString = nbtPathExpression.substring(rightPos, endPos);
        try {
            INbtPathExpression rightExpression = NbtPath.parse(rightExpressionString);
            return new HandleResult(new Expression(rightExpression),
                    matcher.group().length() + rightExpressionString.length());
        } catch (NbtParseException e) {
            return HandleResult.INVALID;
        }
    }

    public static class Expression implements INbtPathExpression {

        protected final INbtPathExpression expression;

        public Expression(INbtPathExpression expression) {
            this.expression = expression;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag currentTag = executionContext.getCurrentTag();

                        // The left side is the current tag (should be a boolean result from previous expression)
                        boolean leftValue = NbtPathExpressionHelpers.isTruthy(currentTag);

                        // Evaluate the right expression against the root context
                        // This ensures both sides of the expression are evaluated against the same base context
                        Tag rootTag = executionContext.getRootContext().getCurrentTag();
                        boolean rightValue = expression.test(rootTag);

                        // AND operation
                        boolean result = leftValue && rightValue;

                        return new NbtPathExpressionExecutionContext(
                                ByteTag.valueOf(result ? (byte) 1 : (byte) 0), executionContext);
                    })
            );
        }
    }
}
