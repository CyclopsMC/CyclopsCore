package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

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

    public static JsonObject serializeItemStack(ItemStack itemStack) {
        JsonObject object = new JsonObject();

        object.addProperty("item", itemStack.getItem().getRegistryName().toString());
        object.addProperty("data", itemStack.getMetadata());
        object.addProperty("count", itemStack.getCount());
        if (itemStack.hasTagCompound()) {
            object.addProperty("nbt", itemStack.getTagCompound().toString());
        }

        return object;
    }

}
