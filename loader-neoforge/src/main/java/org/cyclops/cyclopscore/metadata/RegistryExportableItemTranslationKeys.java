package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item translation key exporter.
 */
public class RegistryExportableItemTranslationKeys implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject element = new JsonObject();

        JsonArray elements = new JsonArray();
        element.add("items", elements);
        for (ResourceLocation key : BuiltInRegistries.ITEM.keySet()) {
            Item value = BuiltInRegistries.ITEM.get(key);
            ItemStack itemStack = new ItemStack(value);
            String translationKey = itemStack.getDescriptionId();

            JsonObject object = new JsonObject();
            object.addProperty("translationKey", translationKey);
            object.add("item", IRegistryExportable.serializeItemStack(itemStack));
            elements.add(object);
        }

        return element;
    }

    @Override
    public String getName() {
        return "item_translation_keys";
    }

}
