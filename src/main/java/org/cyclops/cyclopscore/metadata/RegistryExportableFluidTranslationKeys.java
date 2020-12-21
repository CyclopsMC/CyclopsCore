package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.RegistryKey;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.helper.FluidHelpers;

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
        for (Map.Entry<RegistryKey<Fluid>, Fluid> fluidEntry : ForgeRegistries.FLUIDS.getEntries()) {
            FluidStack value = new FluidStack(fluidEntry.getValue(), FluidHelpers.BUCKET_VOLUME);
            String translationKey = value.getTranslationKey();

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
