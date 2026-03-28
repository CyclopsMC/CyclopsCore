package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
        super(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(category, groupIn),
                new ItemStackTemplate(recipeOutputIn.typeHolder(), recipeOutputIn.getCount(), recipeOutputIn.getComponentsPatch()), recipeItemsIn);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
    }

    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return (RecipeSerializer<ShapelessRecipe>)(Object) this.serializer.getRecipeSerializer();
    }

    @Override
    public ItemStack assemble(CraftingInput inv) {
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
    public static class Serializer {
        private static final Identifier NAME = Identifier.fromNamespaceAndPath("minecraft", "crafting_shapeless");

        private final Supplier<ItemStack> outputProvider;
        @Nullable
        private final Serializer.IOutputTransformer outputTransformer;
        private final RecipeSerializer<RecipeCraftingShapelessCustomOutput> recipeSerializer;

        public Serializer(Supplier<ItemStack> outputProvider, @Nullable Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            MapCodec<RecipeCraftingShapelessCustomOutput> codec = RecordCodecBuilder.mapCodec(
                    p_311734_ -> p_311734_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.group()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category()),
                                    Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(1, ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth())).fieldOf("ingredients").forGetter((p_360071_) -> p_360071_.placementInfo().ingredients())
                            )
                            .apply(p_311734_, (group, category, ingredients) -> new RecipeCraftingShapelessCustomOutput(this, group, category, this.outputProvider.get(), ingredients))
            );
            StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapelessCustomOutput> streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    (p_360074_) -> p_360074_.group(),
                    CraftingBookCategory.STREAM_CODEC,
                    (p_360073_) -> p_360073_.category(),
                    ItemStack.STREAM_CODEC,
                    (p_360070_) -> p_360070_.getResultItem(),
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                    (p_360069_) -> p_360069_.placementInfo().ingredients(),
                    (group, category, result, ingredients) -> new RecipeCraftingShapelessCustomOutput(this, group, category, this.outputProvider.get(), ingredients)
            );
            this.recipeSerializer = new RecipeSerializer<>(codec, streamCodec);
        }

        public Serializer(Supplier<ItemStack> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        public RecipeSerializer<RecipeCraftingShapelessCustomOutput> getRecipeSerializer() {
            return recipeSerializer;
        }

        public static interface IOutputTransformer {
            public ItemStack transform(CraftingInput inventory, ItemStack staticOutput);
        }
    }
}
