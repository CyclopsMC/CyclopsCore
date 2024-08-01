package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.parse.NbtPathStringParser.StringParseResult;

import javax.annotation.Nullable;

/**
 * A handler that handles child path expressions in the form of "["childName"]",
 * where the matched string represents the child name that should be navigated in.
 * This works just like {@link NbtPathExpressionParseHandlerChild},
 * but allows special characters to be used.
 */
public class NbtPathExpressionParseHandlerChildBrackets implements INbtPathExpressionParseHandler {
    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (pos >= nbtPathExpression.length() || nbtPathExpression.charAt(pos) != '[') {
            return HandleResult.INVALID;
        }
        StringParseResult parseResult = NbtPathStringParser.parse(nbtPathExpression, pos + 1);
        if (!parseResult.isSuccess()) {
            return HandleResult.INVALID;
        }
        int closingBracketIndex = pos + parseResult.getConsumed() + 1;
        if (closingBracketIndex >= nbtPathExpression.length() || nbtPathExpression.charAt(closingBracketIndex) != ']') {
            return HandleResult.INVALID;
        }
        return new HandleResult(
            new NbtPathExpressionParseHandlerChild.Expression(parseResult.getResult()),
            2 + parseResult.getConsumed()
        );
    }
}
