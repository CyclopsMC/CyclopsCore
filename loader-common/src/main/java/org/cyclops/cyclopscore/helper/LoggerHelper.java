package org.cyclops.cyclopscore.helper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger that will be used in this mod.
 * @author rubensworks
 *
 */
public class LoggerHelper {

    private final Logger logger;

    /**
     * Initialize the logger.
     * @param modName The mod name.
     */
    public LoggerHelper(String modName) {
        logger = LogManager.getLogger(modName);
    }

    /**
     * Log a new message.
     * @param logLevel The level to log at.
     * @param message The message to log.
     */
    public void log(Level logLevel, String message) {
        logger.log(logLevel, message);
    }

    public Logger getLogger() {
        return logger;
    }
}
