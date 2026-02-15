package org.cyclops.cyclopscore.nbt.path.parse;

import java.util.function.Supplier;

/**
 * A handler that handles boolean OR expressions in the form of "expression1 {@literal ||} expression2".
 */
public class NbtPathExpressionParseHandlerBooleanLogicalOr extends NbtPathExpressionParseHandlerBooleanLogicalAdapter {

    public NbtPathExpressionParseHandlerBooleanLogicalOr() {
        super("\\|\\|");
    }

    @Override
    protected boolean getLogicalValue(boolean left, Supplier<Boolean> right) {
        return left || right.get();
    }
}
