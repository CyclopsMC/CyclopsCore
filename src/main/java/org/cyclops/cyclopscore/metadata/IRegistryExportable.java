package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonObject;

/**
 * A registry export handler.
 */
public interface IRegistryExportable {

    /**
     * @return A JSON representation of the registry.
     */
    public JsonObject export();

    /**
     * @return The unique name of this registry (to be used in file names).
     */
    public String getName();

}
