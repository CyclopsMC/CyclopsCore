package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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
        object.addProperty("count", itemStack.getCount());
        if (itemStack.hasTag()) {
            object.addProperty("nbt", itemStack.getTag().toString());
        }

        return object;
    }

    public static JsonObject serializeFluidStack(FluidStack fluidStack) {
        JsonObject object = new JsonObject();

        object.addProperty("fluid", fluidStack.getFluid().getRegistryName().toString());
        object.addProperty("amount", fluidStack.getAmount());
        if (fluidStack.hasTag()) {
            object.addProperty("nbt", fluidStack.getTag().toString());
        }

        return object;
    }

}
