package org.cyclops.cyclopscore.nbt.path.parse;

import java.util.function.Supplier;

/**
 * A handler that handles boolean AND expressions in the form of "expression1 {@literal &&} expression2".
 */
public class NbtPathExpressionParseHandlerBooleanLogicalAnd extends NbtPathExpressionParseHandlerBooleanLogicalAdapter {

    public NbtPathExpressionParseHandlerBooleanLogicalAnd() {
        super("&&");
    }

    @Override
    protected boolean getLogicalValue(boolean left, Supplier<Boolean> right) {
        return left && right.get();
    }
}
