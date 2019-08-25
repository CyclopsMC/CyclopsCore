package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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
        for (Map.Entry<ResourceLocation, Fluid> fluidEntry : ForgeRegistries.FLUIDS.getEntries()) {
            // TODO: port when Forge has implemented fluids
            /*FluidStack value = new FluidStack(fluidEntry.getValue(), net.minecraftforge.fluids.Fluid.BUCKET_VOLUME);
            String translationKey = value.getUnlocalizedName();

            JsonObject object = new JsonObject();
            object.addProperty("translationKey", translationKey);
            object.add("fluid", IRegistryExportable.serializeFluidStack(value));
            elements.add(object);*/
        }

        return element;
    }

    @Override
    public String getName() {
        return "fluid_translation_keys";
    }

}
