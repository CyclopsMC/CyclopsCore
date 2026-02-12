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
 * A handler that handles boolean AND expressions in the form of "expression1 && expression2".
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

        // Find the end of the right expression (up to the next operator or end of string)
        // For simplicity, we'll parse until we hit another logical operator or closing parenthesis
        int endPos = findExpressionEnd(nbtPathExpression, rightPos);
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

    /**
     * Find the end position of an expression, stopping at logical operators or closing parenthesis.
     */
    private int findExpressionEnd(String expression, int start) {
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
                // Check for NOT operator
                if (c == '!') {
                    return i;
                }
            }
        }
        return expression.length();
    }

    public static class Expression implements INbtPathExpression {

        private final INbtPathExpression rightExpression;

        public Expression(INbtPathExpression rightExpression) {
            this.rightExpression = rightExpression;
        }

        public INbtPathExpression getRightExpression() {
            return rightExpression;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag currentTag = executionContext.getCurrentTag();
                        
                        // The left side is the current tag (should be a boolean result from previous expression)
                        boolean leftValue = isTruthy(currentTag);
                        
                        // Evaluate the right expression against the parent context (original tag before boolean conversion)
                        Tag originalTag = executionContext.getParentContext() != null 
                                ? executionContext.getParentContext().getCurrentTag() 
                                : currentTag;
                        boolean rightValue = rightExpression.test(originalTag);
                        
                        // AND operation
                        boolean result = leftValue && rightValue;
                        
                        return new NbtPathExpressionExecutionContext(
                                ByteTag.valueOf(result ? (byte) 1 : (byte) 0), executionContext);
                    })
            );
        }

        /**
         * Determine if a tag is truthy.
         * ByteTag with value 1 is true, 0 is false.
         * Any other non-null tag is considered true.
         */
        private boolean isTruthy(Tag tag) {
            if (tag == null) {
                return false;
            }
            if (tag.getId() == Tag.TAG_BYTE) {
                return ((ByteTag) tag).getAsByte() == (byte) 1;
            }
            // Non-null tags are truthy
            return true;
        }
    }
}
