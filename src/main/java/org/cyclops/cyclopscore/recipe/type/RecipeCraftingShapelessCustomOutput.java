package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapelessCustomOutput extends ShapelessRecipe {

    private final RecipeCraftingShapelessCustomOutput.Serializer serializer;
    private final ItemStack recipeOutput;

    public RecipeCraftingShapelessCustomOutput(RecipeCraftingShapelessCustomOutput.Serializer serializer, String groupIn, CraftingBookCategory category, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(groupIn, category, recipeOutputIn, recipeItemsIn);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
    }

    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
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

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return getResultItem();
    }

    // Partially copied from ShapelessRecipe.Serializer
    public static class Serializer implements RecipeSerializer<RecipeCraftingShapelessCustomOutput> {
        private static final net.minecraft.resources.ResourceLocation NAME = ResourceLocation.fromNamespaceAndPath("minecraft", "crafting_shapeless");

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
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_301127_ -> p_301127_.getGroup()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_301133_ -> p_301133_.category()),
//                                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(p_301142_ -> p_301142_.getRecipeOutput()), // This is removed
                                    Ingredient.CODEC_NONEMPTY
                                            .listOf()
                                            .fieldOf("ingredients")
                                            .flatXmap(
                                                    p_301021_ -> {
                                                        Ingredient[] aingredient = p_301021_
                                                                .toArray(Ingredient[]::new); //Forge skip the empty check and immediatly create the array.
                                                        if (aingredient.length == 0) {
                                                            return DataResult.error(() -> "No ingredients for shapeless recipe");
                                                        } else {
                                                            return aingredient.length > 3 * 3
                                                                    ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(3 * 3))
                                                                    : DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                                        }
                                                    },
                                                    DataResult::success
                                            )
                                            .forGetter(p_300975_ -> p_300975_.getIngredients())
                            )
                            .apply(p_311734_, (group, category, ingredients) -> new RecipeCraftingShapelessCustomOutput(this, group, category, this.outputProvider.get(), ingredients)) // This line is different
            );
            this.streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
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

        private RecipeCraftingShapelessCustomOutput fromNetwork(RegistryFriendlyByteBuf p_319905_) {
            String s = p_319905_.readUtf();
            CraftingBookCategory craftingbookcategory = p_319905_.readEnum(CraftingBookCategory.class);
            int i = p_319905_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(p_319735_ -> Ingredient.CONTENTS_STREAM_CODEC.decode(p_319905_));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(p_319905_);
            return new RecipeCraftingShapelessCustomOutput(this, s, craftingbookcategory, itemstack, nonnulllist);
        }

        private void toNetwork(RegistryFriendlyByteBuf p_320371_, RecipeCraftingShapelessCustomOutput p_320323_) {
            p_320371_.writeUtf(p_320323_.getGroup());
            p_320371_.writeEnum(p_320323_.category());
            p_320371_.writeVarInt(p_320323_.getIngredients().size());

            for (Ingredient ingredient : p_320323_.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(p_320371_, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(p_320371_, p_320323_.getResultItem());
        }

        public static interface IOutputTransformer {
            public ItemStack transform(CraftingInput inventory, ItemStack staticOutput);
        }
    }
}
