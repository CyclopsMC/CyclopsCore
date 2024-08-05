package org.cyclops.cyclopscore.config;

/**
 * Copied from NeoForge's ModConfig.Type to be aligned with it.
 * @author rubensworks
 */
public enum ModConfigLocation {
    /**
     * Common mod config for configuration that needs to be loaded on both environments.
     * Loaded on both servers and clients.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-common" by default.
     */
    COMMON,
    /**
     * Client config is for configuration affecting the ONLY client state such as graphical options.
     * Only loaded on the client side.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-client" by default.
     */
    CLIENT,
    /**
     * Server type config is configuration that is associated with a server instance.
     * Only loaded during server startup.
     * Stored in a server/save specific "serverconfig" directory.
     * Synced to clients during connection.
     * Suffix is "-server" by default.
     */
    SERVER,
    /**
     * Startup configs are for configurations that need to run as early as possible.
     * Loaded as soon as the config is registered to FML.
     * Please be aware when using them, as using these configs to enable/disable registration and anything that must be present on both sides
     * can cause clients to have issues connecting to servers with different config values.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-startup" by default.
     */
    STARTUP;
}
