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
 * A handler that handles boolean NOT expressions in the form of "!expression".
 */
public class NbtPathExpressionParseHandlerBooleanLogicalNot implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_EXPRESSION = Pattern.compile("^ *! *");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_EXPRESSION
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        // Parse the expression to negate
        int exprPos = pos + matcher.group().length();
        if (exprPos >= nbtPathExpression.length()) {
            return HandleResult.INVALID;
        }

        // Find the end of the expression
        int endPos = findExpressionEnd(nbtPathExpression, exprPos);
        if (endPos == exprPos) {
            return HandleResult.INVALID;
        }

        String expressionString = nbtPathExpression.substring(exprPos, endPos);
        try {
            INbtPathExpression expression = NbtPath.parse(expressionString);
            return new HandleResult(new Expression(expression),
                    matcher.group().length() + expressionString.length());
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
            }
        }
        return expression.length();
    }

    public static class Expression implements INbtPathExpression {

        private final INbtPathExpression expression;

        public Expression(INbtPathExpression expression) {
            this.expression = expression;
        }

        public INbtPathExpression getExpression() {
            return expression;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag currentTag = executionContext.getCurrentTag();
                        
                        // Evaluate the expression
                        boolean value = expression.test(currentTag);
                        
                        // NOT operation
                        boolean result = !value;
                        
                        return new NbtPathExpressionExecutionContext(
                                ByteTag.valueOf(result ? (byte) 1 : (byte) 0), executionContext);
                    })
            );
        }
    }
}
