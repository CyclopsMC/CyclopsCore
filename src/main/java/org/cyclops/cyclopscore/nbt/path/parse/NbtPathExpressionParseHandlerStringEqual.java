package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;
import org.cyclops.cyclopscore.nbt.path.parse.NbtPathStringParser.StringParseResult;

import java.util.stream.Stream;

/**
 * A handler that handles boolean expressions in the form of " == "abc"".
 */
public class NbtPathExpressionParseHandlerStringEqual implements INbtPathExpressionParseHandler {
    /**
     * Skips all consecutive spaces.
     * @param string Source string
     * @param pos Index of the first potential space
     * @return Index of first encountered non space character
     */
    private static int skipSpaces(String string, int pos) {
        while (pos < string.length() && string.charAt(pos) == ' ') {
            pos++;
        }
        return pos;
    }

    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        int currentPos = skipSpaces(nbtPathExpression, pos);
        if ((currentPos + 1) >= nbtPathExpression.length()) {
            return HandleResult.INVALID;
        }
        if (nbtPathExpression.charAt(currentPos) != '=' || nbtPathExpression.charAt(currentPos + 1) != '=') {
            return HandleResult.INVALID;
        }
        currentPos = skipSpaces(nbtPathExpression, currentPos + 2);
        StringParseResult parseResult = NbtPathStringParser.parse(nbtPathExpression, currentPos);
        if (!parseResult.isSuccess()) {
            return HandleResult.INVALID;
        }
        return new HandleResult(
            new Expression(parseResult.getResult()),
            currentPos - pos + parseResult.getConsumed()
        );
    }

    public static class Expression implements INbtPathExpression {

        private final String targetString;

        public Expression(String targetString) {
            this.targetString = targetString;
        }

        String getTargetString() {
            return targetString;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        INBT nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Constants.NBT.TAG_STRING) {
                            StringNBT tag = (StringNBT) nbt;
                            return new NbtPathExpressionExecutionContext(
                                    ByteNBT.valueOf(getTargetString().equals(tag.getString())
                                            ? (byte) 1 : (byte) 0), executionContext);
                        }
                        return new NbtPathExpressionExecutionContext(ByteNBT.valueOf((byte) 0), executionContext);
                    })
            );
        }

    }
}
