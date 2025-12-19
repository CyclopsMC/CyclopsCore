package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapelessCustomOutput extends ShapelessRecipe {

    private final RecipeCraftingShapelessCustomOutput.Serializer serializer;
    private final ItemStack recipeOutput;

    public RecipeCraftingShapelessCustomOutput(RecipeCraftingShapelessCustomOutput.Serializer serializer, String groupIn, CraftingBookCategory category, ItemStack recipeOutputIn, List<Ingredient> recipeItemsIn) {
        super(groupIn, category, recipeOutputIn, recipeItemsIn);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
    }

    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer) this.serializer;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        Serializer.IOutputTransformer outputTransformer = serializer.getOutputTransformer();
        if (outputTransformer != null) {
            return outputTransformer.transform(inv, this.getResultItem());
        }
        return this.getResultItem().copy();
    }

    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    // Partially copied from ShapelessRecipe.Serializer
    public static class Serializer implements RecipeSerializer<RecipeCraftingShapelessCustomOutput> {
        private static final net.minecraft.resources.Identifier NAME = Identifier.fromNamespaceAndPath("minecraft", "crafting_shapeless");

        private final Supplier<ItemStack> outputProvider;
        @Nullable
        private final Serializer.IOutputTransformer outputTransformer;
        private final MapCodec<RecipeCraftingShapelessCustomOutput> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapelessCustomOutput> streamCodec;

        public Serializer(Supplier<ItemStack> outputProvider, @Nullable Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_311734_ -> p_311734_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.group()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category()),
//                                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.getRecipeOutput()), // This is removed
                                    Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())).fieldOf("ingredients").forGetter((p_360071_) -> p_360071_.placementInfo().ingredients())
                            )
                            .apply(p_311734_, (group, category, ingredients) -> new RecipeCraftingShapelessCustomOutput(this, group, category, this.outputProvider.get(), ingredients)) // This line is different
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    (p_360074_) -> p_360074_.group(),
                    CraftingBookCategory.STREAM_CODEC,
                    (p_360073_) -> p_360073_.category(),
                    ItemStack.STREAM_CODEC,
                    (p_360070_) -> p_360070_.getResultItem(),
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (p_360069_) -> p_360069_.placementInfo().ingredients(),
                    (group, category, result, ingredients) -> new RecipeCraftingShapelessCustomOutput(this, group, category, this.outputProvider.get(), ingredients) // This line is different
            );
        }

        public Serializer(Supplier<ItemStack> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        @Override
        public MapCodec<RecipeCraftingShapelessCustomOutput> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapelessCustomOutput> streamCodec() {
            return streamCodec;
        }

        public static interface IOutputTransformer {
            public ItemStack transform(CraftingInput inventory, ItemStack staticOutput);
        }
    }
}
