package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

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

        object.addProperty("item", BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());
        object.addProperty("count", itemStack.getCount());
        if (itemStack.hasTag()) {
            object.addProperty("nbt", itemStack.getTag().toString());
        }

        return object;
    }

    public static JsonObject serializeFluidStack(FluidStack fluidStack) {
        JsonObject object = new JsonObject();

        object.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString());
        object.addProperty("amount", fluidStack.getAmount());
        if (fluidStack.hasTag()) {
            object.addProperty("nbt", fluidStack.getTag().toString());
        }

        return object;
    }

}
