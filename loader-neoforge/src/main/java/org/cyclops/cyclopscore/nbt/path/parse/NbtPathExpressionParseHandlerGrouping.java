package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A handler that handles parenthesized expressions for grouping: "(expression)".
 * This allows precedence control in complex logical expressions.
 */
public class NbtPathExpressionParseHandlerGrouping implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_EXPRESSION = Pattern.compile("^ *\\(");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_EXPRESSION
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        // Find the matching closing parenthesis
        int openPos = pos + matcher.group().length() - 1; // Position of '('
        int closePos = findMatchingClosingParenthesis(nbtPathExpression, openPos);
        if (closePos == -1) {
            return HandleResult.INVALID;
        }

        // Extract the expression inside the parentheses
        String innerExpression = nbtPathExpression.substring(openPos + 1, closePos);

        try {
            // Parse the inner expression
            INbtPathExpression expression = NbtPath.parse(innerExpression.trim());

            // The grouping itself doesn't change the expression, it just controls precedence
            // So we return the inner expression directly wrapped in a pass-through
            return new HandleResult(expression, matcher.group().length() + innerExpression.length() + 1);
        } catch (NbtParseException e) {
            return HandleResult.INVALID;
        }
    }

    /**
     * Find the matching closing parenthesis for an opening parenthesis.
     * @param expression The expression string
     * @param openPos The position of the opening parenthesis
     * @return The position of the matching closing parenthesis, or -1 if not found
     */
    private int findMatchingClosingParenthesis(String expression, int openPos) {
        int depth = 0;
        for (int i = openPos; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}
