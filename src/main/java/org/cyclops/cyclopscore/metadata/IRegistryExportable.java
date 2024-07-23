package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.CyclopsCore;

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
        String componentsString = "{}";
        try {
            componentsString = componentsToString(ServerLifecycleHooks.getCurrentServer().registryAccess(), itemStack.getComponentsPatch());
        } catch (IllegalStateException e) {
            CyclopsCore.clog(e.getMessage());
        }
        if(!"{}".equals(componentsString)) {
            object.addProperty("components", componentsString);
        }

        return object;
    }

    public static JsonObject serializeFluidStack(FluidStack fluidStack) {
        JsonObject object = new JsonObject();

        object.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString());
        object.addProperty("amount", fluidStack.getAmount());
        String componentsString = "{}";
        try {
            componentsString = componentsToString(ServerLifecycleHooks.getCurrentServer().registryAccess(), fluidStack.getComponentsPatch());
        } catch (IllegalStateException e) {
            CyclopsCore.clog(e.getMessage());
        }
        if(!"{}".equals(componentsString)) {
            object.addProperty("components", componentsString);
        }

        return object;
    }

    public static String componentsToString(HolderLookup.Provider lookupProvider, DataComponentPatch components) {
        return DataComponentPatch.CODEC.encodeStart(lookupProvider.createSerializationContext(NbtOps.INSTANCE), components).getOrThrow().toString();
    }

}
