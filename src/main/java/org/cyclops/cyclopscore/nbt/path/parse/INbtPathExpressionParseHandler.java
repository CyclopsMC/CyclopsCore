package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;

import javax.annotation.Nullable;

/**
 * Handles a string representation of an NBT path expression.
 */
public interface INbtPathExpressionParseHandler {

    /**
     * Try to create an expression from the given position in the given expression.
     * @param nbtPathExpression A string representation of an NBT path expression.
     * @param pos The position in the string to start from.
     * @return The handler result.
     */
    public INbtPathExpressionParseHandler.HandleResult handlePrefixOf(String nbtPathExpression, int pos);

    /**
     * A result data object for {@link INbtPathExpressionParseHandler}.
     */
    public static class HandleResult {

        public static final HandleResult INVALID = new HandleResult(null, 0);

        @Nullable
        private final INbtPathExpression prefixExpression;
        private final int consumedExpressionLength;

        public HandleResult(@Nullable INbtPathExpression prefixExpression, int consumedExpressionLength) {
            this.prefixExpression = prefixExpression;
            this.consumedExpressionLength = consumedExpressionLength;
        }

        /**
         * @return If the handler could produce a valid expression.
         */
        public boolean isValid() {
            return getPrefixExpression() != null;
        }

        /**
         * @return The expression (for a part) of the given string expression.
         */
        @Nullable
        public INbtPathExpression getPrefixExpression() {
            return prefixExpression;
        }

        /**
         * @return The length of the string expression that was consumed.
         */
        public int getConsumedExpressionLength() {
            return consumedExpressionLength;
        }
    }

}
