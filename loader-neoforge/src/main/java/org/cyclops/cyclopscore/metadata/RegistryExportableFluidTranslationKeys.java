package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;

import java.util.Map;

/**
 * Fluid translation key exporter.
 */
public class RegistryExportableFluidTranslationKeys implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject element = new JsonObject();

        JsonArray elements = new JsonArray();
        element.add("fluids", elements);
        for (Map.Entry<ResourceKey<Fluid>, Fluid> fluidEntry : BuiltInRegistries.FLUID.entrySet()) {
            FluidStack value = new FluidStack(fluidEntry.getValue(), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume());
            String translationKey = value.getFluidType().getDescriptionId(value);

            JsonObject object = new JsonObject();
            object.addProperty("translationKey", translationKey);
            object.add("fluid", IRegistryExportable.serializeFluidStack(value));
            elements.add(object);
        }

        return element;
    }

    @Override
    public String getName() {
        return "fluid_translation_keys";
    }

}
