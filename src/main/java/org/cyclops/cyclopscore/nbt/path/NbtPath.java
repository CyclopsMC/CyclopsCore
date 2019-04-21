package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import org.cyclops.cyclopscore.nbt.path.parse.*;

import java.util.List;

/**
 * Utility class for parsing NBT path expressions.
 */
public class NbtPath {

    private static final List<INbtPathExpressionParseHandler> PARSE_HANDLERS = Lists.newArrayList(
            new NbtPathExpressionParseHandlerRoot(),
            new NbtPathExpressionParseHandlerLength(),
            new NbtPathExpressionParseHandlerChild(),
            new NbtPathExpressionParseHandlerParent(),
            new NbtPathExpressionParseHandlerAllChildren(),
            new NbtPathExpressionParseHandlerCurrent(),
            new NbtPathExpressionParseHandlerListElement(),
            new NbtPathExpressionParseHandlerListSlice(),
            new NbtPathExpressionParseHandlerUnion()
    );

    /**
     * Parse an NBT path expression string into an in-memory representation.
     * @param nbtPathExpression An NBT path expression string
     * @return An in-memory representation of the given expression.
     * @throws NbtParseException An exception that can be thrown if parsing failed.
     */
    public static INbtPathExpression parse(String nbtPathExpression) throws NbtParseException {
        List<INbtPathExpression> expressions = Lists.newArrayList();

        int pos = 0;
        while (pos < nbtPathExpression.length()) {
            boolean handled = false;
            for (INbtPathExpressionParseHandler parseHandler : NbtPath.PARSE_HANDLERS) {
                INbtPathExpressionParseHandler.HandleResult handleResult = parseHandler.handlePrefixOf(nbtPathExpression, pos);
                if (handleResult.isValid()) {
                    pos += handleResult.getConsumedExpressionLength();
                    expressions.add(handleResult.getPrefixExpression());
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                throw new NbtParseException(String.format("Failed to parse expression at pos '%s'",
                        pos, nbtPathExpression));
            }
        }

        return new NbtPathExpressionList(expressions.toArray(new INbtPathExpression[0]));
    }

}
