package org.cyclops.cyclopscore.config;

/**
 * Exceptions that can occur when configuring this mod.
 * @author rubensworks
 *
 */
public class CyclopsCoreConfigException extends RuntimeException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Make a new instance.
     * @param message The message.
     */
    public CyclopsCoreConfigException(String message) {
        super(message);
    }
    
}
