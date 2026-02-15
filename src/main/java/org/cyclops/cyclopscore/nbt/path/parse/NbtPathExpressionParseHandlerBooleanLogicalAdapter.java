package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author rubensworks
 */
public abstract class NbtPathExpressionParseHandlerBooleanLogicalAdapter implements INbtPathExpressionParseHandler {

    private final Pattern regex;

    protected NbtPathExpressionParseHandlerBooleanLogicalAdapter(String relation) {
        this.regex = Pattern.compile("^ *" + relation + " *");
    }

    protected abstract boolean getLogicalValue(boolean left, Supplier<Boolean> right);

    /**
     * Determine if a tag is truthy.
     * ByteTag with value 1 is true, 0 is false.
     * Any other non-null tag is considered true.
     * This follows the same logic as {@link org.cyclops.cyclopscore.nbt.path.INbtPathExpression#test(Tag)}.
     *
     * @param tag The tag to check
     * @return true if the tag is truthy, false otherwise
     */
    public static boolean isTruthy(Tag tag) {
        if (tag == null) {
            return false;
        }
        if (tag.getId() == Tag.TAG_BYTE) {
            return ((ByteTag) tag).getAsByte() == (byte) 1;
        }
        // Non-null non-ByteTags are truthy
        return true;
    }

    /**
     * Find the end position of an expression, stopping at logical operators or closing parenthesis.
     * This method is shared by logical operator handlers to identify expression boundaries.
     *
     * @param expression The full expression string
     * @param start The starting position to search from
     * @return The position where the expression ends
     */
    public static int findExpressionEnd(String expression, int start) {
        int depth = 0;
        for (int i = start; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                if (depth == 0) {
                    return i;
                }
                depth--;
            } else if (depth == 0) {
                // Check for logical operators at top level
                if (i + 1 < expression.length()) {
                    String twoChar = expression.substring(i, i + 2);
                    if (twoChar.equals("&&") || twoChar.equals("||")) {
                        return i;
                    }
                }
                // Check for NOT operator (but not != which is handled differently)
                if (c == '!' && (i + 1 >= expression.length() || expression.charAt(i + 1) != '=')) {
                    return i;
                }
            }
        }
        return expression.length();
    }

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = this.regex
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
        int endPos = findExpressionEnd(nbtPathExpression, rightPos);
        if (endPos == rightPos) {
            return HandleResult.INVALID;
        }

        String rightExpressionString = nbtPathExpression.substring(rightPos, endPos);
        try {
            INbtPathExpression rightExpression = NbtPath.parse(rightExpressionString.trim());
            return new HandleResult(new Expression(rightExpression, this),
                    matcher.group().length() + rightExpressionString.length());
        } catch (NbtParseException e) {
            return HandleResult.INVALID;
        }
    }

    public static class Expression implements INbtPathExpression {

        protected final INbtPathExpression expression;
        protected final NbtPathExpressionParseHandlerBooleanLogicalAdapter handler;

        public Expression(INbtPathExpression expression, NbtPathExpressionParseHandlerBooleanLogicalAdapter handler) {
            this.expression = expression;
            this.handler = handler;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag currentTag = executionContext.getCurrentTag();

                        // The left side is the current tag (should be a boolean result from previous expression)
                        boolean leftValue = isTruthy(currentTag);

                        // Evaluate the right expression against the root context
                        // This ensures both sides of the expression are evaluated against the same base context
                        Tag rootTag = executionContext.getRootContext().getCurrentTag();

                        // AND operation
                        boolean result = handler.getLogicalValue(leftValue, () -> expression.test(rootTag));

                        return new NbtPathExpressionExecutionContext(ByteTag.valueOf(result), executionContext);
                    })
            );
        }

    }

}
