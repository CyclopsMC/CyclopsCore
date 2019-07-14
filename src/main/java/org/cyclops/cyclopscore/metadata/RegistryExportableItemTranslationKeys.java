package org.cyclops.cyclopscore.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

/**
 * Crafting recipe exporter.
 */
public class RegistryExportableItemTranslationKeys implements IRegistryExportable {

    @Override
    public JsonObject export() {
        JsonObject element = new JsonObject();

        JsonArray elements = new JsonArray();
        element.add("items", elements);
        for (ResourceLocation key : Item.REGISTRY.getKeys()) {
            Item value = Item.REGISTRY.getObject(key);
            NonNullList<ItemStack> subItems = NonNullList.create();
            value.getSubItems(CreativeTabs.SEARCH, subItems);
            for (ItemStack subItem : subItems) {
                String translationKey = subItem.getTranslationKey();

                JsonObject object = new JsonObject();
                object.addProperty("translationKey", translationKey);
                object.add("item", IRegistryExportable.serializeItemStack(subItem));
                elements.add(object);
            }
        }

        return element;
    }

    @Override
    public String getName() {
        return "item_translation_keys";
    }

}
