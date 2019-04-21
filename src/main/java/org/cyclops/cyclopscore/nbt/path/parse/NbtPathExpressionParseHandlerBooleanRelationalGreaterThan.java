package org.cyclops.cyclopscore.nbt.path.parse;

/**
 * A handler that handles boolean expressions in the form of " > 10".
 */
public class NbtPathExpressionParseHandlerBooleanRelationalGreaterThan extends NbtPathExpressionParseHandlerBooleanRelationalAdapter {

    public NbtPathExpressionParseHandlerBooleanRelationalGreaterThan() {
        super(">");
    }

    protected boolean getRelationalValue(double left, double right) {
        return left > right;
    }
}
