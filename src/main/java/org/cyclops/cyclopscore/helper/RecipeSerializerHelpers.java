package org.cyclops.cyclopscore.helper;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.recipe.ItemStackFromIngredient;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Helpers related to recipe serialization.
 * @author rubensworks
 */
public class RecipeSerializerHelpers {

    public static Codec<ItemStackFromIngredient> getCodecItemStackFromIngredient(Supplier<List<String>> modPriorities) {
        return RecordCodecBuilder.create(
                builder -> builder.group(
                                Codec.STRING.fieldOf("tag").forGetter(ItemStackFromIngredient::getTag),
                                Codec.INT.optionalFieldOf("count").forGetter(i -> Optional.of(i.getCount()))
                        )
                        .apply(builder, (tag, count) -> new ItemStackFromIngredient(modPriorities.get(), tag, Ingredient.fromValues(Stream.of(new Ingredient.TagValue(TagKey.create(Registries.ITEM, ResourceLocation.parse(tag))))), count.orElse(1)))
        );
    }

    public static Codec<Either<ItemStack, ItemStackFromIngredient>> getCodecItemStackOrTag(Supplier<List<String>> modPriorities) {
        return Codec.either(ItemStack.CODEC, getCodecItemStackFromIngredient(modPriorities));
    }

    public static Codec<Either<Pair<ItemStack, Float>, Pair<ItemStackFromIngredient, Float>>> getCodecItemStackOrTagChance(Supplier<List<String>> modPriorities) {
        return Codec.either(
                RecordCodecBuilder.create(
                        builder -> builder.group(
                                ItemStack.CODEC.fieldOf("item").forGetter(Pair::getLeft),
                                Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(Pair::getRight)
                        ).apply(builder, Pair::of)
                ),
                RecordCodecBuilder.create(
                        builder -> builder.group(
                                RecipeSerializerHelpers.getCodecItemStackFromIngredient(modPriorities).fieldOf("tag").forGetter(Pair::getLeft),
                                Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(Pair::getRight)
                        ).apply(builder, Pair::of)
                )
        );
    }

    public static void writeItemStackOrItemStackIngredient(RegistryFriendlyByteBuf buffer, Either<ItemStack, ItemStackFromIngredient> itemStackOrItemStackIngredient) {
        itemStackOrItemStackIngredient.mapBoth(
                itemStack -> {
                    buffer.writeBoolean(true);
                    ItemStack.STREAM_CODEC.encode(buffer, itemStack);
                    return null;
                },
                ingredient -> {
                    buffer.writeBoolean(false);
                    ingredient.writeToPacket(buffer);
                    return null;
                }
        );
    }

    public static Either<ItemStack, ItemStackFromIngredient> readItemStackOrItemStackIngredient(RegistryFriendlyByteBuf buffer) {
        Either<ItemStack, ItemStackFromIngredient> outputItem;
        if (buffer.readBoolean()) {
            outputItem = Either.left(ItemStack.STREAM_CODEC.decode(buffer));
        } else {
            outputItem = Either.right(ItemStackFromIngredient.readFromPacket(buffer));
        }
        return outputItem;
    }

    public static void writeItemStackOrItemStackIngredientChance(RegistryFriendlyByteBuf buffer, Either<Pair<ItemStack, Float>, Pair<ItemStackFromIngredient, Float>> itemStackOrItemStackIngredient) {
        itemStackOrItemStackIngredient.mapBoth(
                itemStack -> {
                    buffer.writeBoolean(true);
                    ItemStack.STREAM_CODEC.encode(buffer, itemStack.getLeft());
                    buffer.writeFloat(itemStack.getRight());
                    return null;
                },
                ingredient -> {
                    buffer.writeBoolean(false);
                    ingredient.getLeft().writeToPacket(buffer);
                    buffer.writeFloat(ingredient.getRight());
                    return null;
                }
        );
    }

    public static Either<Pair<ItemStack, Float>, Pair<ItemStackFromIngredient, Float>> readItemStackOrItemStackIngredientChance(RegistryFriendlyByteBuf buffer) {
        Either<Pair<ItemStack, Float>, Pair<ItemStackFromIngredient, Float>> outputItem;
        if (buffer.readBoolean()) {
            outputItem = Either.left(Pair.of(ItemStack.STREAM_CODEC.decode(buffer), buffer.readFloat()));
        } else {
            outputItem = Either.right(Pair.of(ItemStackFromIngredient.readFromPacket(buffer), buffer.readFloat()));
        }
        return outputItem;
    }

    public static <T> void writeOptionalToNetwork(FriendlyByteBuf buffer, Optional<T> value, BiConsumer<FriendlyByteBuf, T> writeToBuffer) {
        value.ifPresentOrElse(v -> {
            buffer.writeBoolean(true);
            writeToBuffer.accept(buffer, v);
        }, () -> buffer.writeBoolean(false));
    }

    public static <T> Optional<T> readOptionalFromNetwork(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> readFromBuffer) {
        return buffer.readBoolean() ? Optional.of(readFromBuffer.apply(buffer)) : Optional.empty();
    }

}
