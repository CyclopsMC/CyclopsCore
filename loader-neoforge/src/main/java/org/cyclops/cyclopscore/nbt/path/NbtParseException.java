package org.cyclops.cyclopscore.nbt.path;

/**
 * An exception that can be thrown during the parsing of NBT path expressions.
 */
public class NbtParseException extends Exception {

    public NbtParseException(String msg) {
        super(msg);
    }

}
