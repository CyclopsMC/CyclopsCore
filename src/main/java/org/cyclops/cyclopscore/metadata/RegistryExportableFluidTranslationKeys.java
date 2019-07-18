package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
        for (Map.Entry<String, Fluid> fluidEntry : FluidRegistry.getRegisteredFluids().entrySet()) {
            FluidStack value = new FluidStack(fluidEntry.getValue(), Fluid.BUCKET_VOLUME);
            String translationKey = value.getUnlocalizedName();

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
