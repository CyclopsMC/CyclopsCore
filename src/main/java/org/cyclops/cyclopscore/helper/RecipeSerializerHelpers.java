package org.cyclops.cyclopscore.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;

import java.util.Collections;
import java.util.List;

/**
 * Helpers related to recipe serialization.
 * @author rubensworks
 */
public class RecipeSerializerHelpers {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static Ingredient getJsonIngredient(JsonObject json, String key, boolean required) {
        JsonElement element = json.get(key);
        if (element == null) {
            if (required) {
                throw new JsonSyntaxException("Missing " + key + ", expected to find an ingredient object or string value");
            } else {
                return Ingredient.EMPTY;
            }
        } else if (element.isJsonObject()) {
            return Ingredient.fromJson(GsonHelper.getAsJsonObject(json, key));
        } else if (element.isJsonArray()) {
            return Ingredient.fromJson(GsonHelper.getAsJsonArray(json, key));
        } else {
            String itemName = GsonHelper.getAsString(json, key);
            ResourceLocation resourcelocation = new ResourceLocation(itemName);
            return Ingredient.of(new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation)
                    .orElseThrow(() -> new JsonSyntaxException("Item: " + itemName + " does not exist"))));
        }
    }

    @Deprecated
    public static Either<ItemStack, ItemStackFromIngredient> getJsonItemStackOrTag(JsonObject json, boolean required) {
        return getJsonItemStackOrTag(json, required, Collections.emptyList());
    }

    public static Either<ItemStack, ItemStackFromIngredient> getJsonItemStackOrTag(JsonObject json, boolean required, List<String> modPriorities) {
        if (json.has("tag")) {
            return Either.right(getJsonItemStackFromTag(json, "tag", modPriorities));
        }
        return Either.left(getJsonItemStack(json, "item", required));
    }

    public static ItemStack getJsonItemStack(JsonObject json, String key, boolean required) {
        JsonElement element = json.get(key);
        if (element == null) {
            if (required) {
                throw new JsonSyntaxException("Missing " + key + ", expected to find an item object or string value");
            } else {
                return ItemStack.EMPTY;
            }
        } else if (element.isJsonObject()) {
            return ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, key));
        } else {
            String itemName = GsonHelper.getAsString(json, key);
            ResourceLocation resourcelocation = new ResourceLocation(itemName);
            return new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation)
                    .orElseThrow(() -> new JsonSyntaxException("Item: " + itemName + " does not exist")));
        }
    }

    @Deprecated
    public static ItemStackFromIngredient getJsonItemStackFromTag(JsonObject json, String key) {
        return getJsonItemStackFromTag(json, key, Collections.emptyList());
    }

    public static ItemStackFromIngredient getJsonItemStackFromTag(JsonObject json, String key, List<String> modPriorities) {
        // Determine count
        int count = 1;
        if (json.has("count")) {
            count = json.get("count").getAsInt();
        }

        return new ItemStackFromIngredient(modPriorities, key, Ingredient.fromJson(json), count);
    }

    public static FluidStack deserializeFluidStack(JsonObject json, boolean readNbt) {
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }

        // Read Fluid
        String fluidName = GsonHelper.getAsString(json, "fluid");
        ResourceLocation resourcelocation = new ResourceLocation(fluidName);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourcelocation);
        if (fluid instanceof EmptyFluid) {
            throw new JsonParseException("Unknown fluid '" + fluidName + "'");
        }

        // Read NBT
        CompoundTag tag = null;
        if (readNbt && json.has("nbt")) {
            try {
                JsonElement element = json.get("nbt");
                if (element.isJsonObject()) {
                    tag = TagParser.parseTag(GSON.toJson(element));
                } else {
                    tag = TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
                }
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }

        // Read amount
        int amount = FluidHelpers.BUCKET_VOLUME;
        if (json.has("amount")) {
            amount = GsonHelper.getAsInt(json, "amount");
        }

        return new FluidStack(fluid, amount, tag);
    }

    public static FluidStack getJsonFluidStack(JsonObject json, String key, boolean required) {
        JsonElement element = json.get(key);
        if (element == null) {
            if (required) {
                throw new JsonSyntaxException("Missing " + key + ", expected to find a fluid object or string value");
            } else {
                return FluidStack.EMPTY;
            }
        } else if (element.isJsonObject()) {
            return deserializeFluidStack(GsonHelper.getAsJsonObject(json, key), true);
        } else {
            String fluidName = GsonHelper.getAsString(json, key);
            ResourceLocation resourcelocation = new ResourceLocation(fluidName);
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourcelocation);
            if (fluid instanceof EmptyFluid) {
                throw new JsonParseException("Unknown fluid '" + fluidName + "'");
            }
            return new FluidStack(fluid, FluidHelpers.BUCKET_VOLUME);
        }
    }

    public static void writeItemStackOrItemStackIngredient(FriendlyByteBuf buffer, Either<ItemStack, ItemStackFromIngredient> itemStackOrItemStackIngredient) {
        itemStackOrItemStackIngredient.mapBoth(
                itemStack -> {
                    buffer.writeBoolean(true);
                    buffer.writeItem(itemStack);
                    return null;
                },
                ingredient -> {
                    buffer.writeBoolean(false);
                    ingredient.writeToPacket(buffer);
                    return null;
                }
        );
    }

    public static Either<ItemStack, ItemStackFromIngredient> readItemStackOrItemStackIngredient(FriendlyByteBuf buffer) {
        Either<ItemStack, ItemStackFromIngredient> outputItem;
        if (buffer.readBoolean()) {
            outputItem = Either.left(buffer.readItem());
        } else {
            outputItem = Either.right(ItemStackFromIngredient.readFromPacket(buffer));
        }
        return outputItem;
    }

}
