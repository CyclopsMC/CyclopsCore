package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
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
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }

    // Partially copied from ShapelessRecipe.Serializer
    public static class Serializer implements RecipeSerializer<RecipeCraftingShapelessCustomOutput> {
        private static final net.minecraft.resources.ResourceLocation NAME = new net.minecraft.resources.ResourceLocation("minecraft", "crafting_shapeless");

        private final Supplier<ItemStack> outputProvider;
        @Nullable
        private final Serializer.IOutputTransformer outputTransformer;
        private final Codec<RecipeCraftingShapelessCustomOutput> codec;

        public Serializer(Supplier<ItemStack> outputProvider, @Nullable Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            this.codec = RecordCodecBuilder.create(
                    p_311734_ -> p_311734_.group(
                                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(p_301127_ -> p_301127_.getGroup()),
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
        }

        public Serializer(Supplier<ItemStack> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        @Override
        public Codec<RecipeCraftingShapelessCustomOutput> codec() {
            return codec;
        }

        public RecipeCraftingShapelessCustomOutput fromNetwork(FriendlyByteBuf p_44294_) {
            String s = p_44294_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44294_.readEnum(CraftingBookCategory.class);
            int i = p_44294_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(p_44294_));
            }

            ItemStack itemstack = p_44294_.readItem();
            return new RecipeCraftingShapelessCustomOutput(this, s, craftingbookcategory, itemstack, nonnulllist);
        }

        public void toNetwork(FriendlyByteBuf p_44281_, RecipeCraftingShapelessCustomOutput p_44282_) {
            p_44281_.writeUtf(p_44282_.getGroup());
            p_44281_.writeEnum(p_44282_.category());
            p_44281_.writeVarInt(p_44282_.getIngredients().size());

            for(Ingredient ingredient : p_44282_.getIngredients()) {
                ingredient.toNetwork(p_44281_);
            }

            p_44281_.writeItem(p_44282_.getRecipeOutput());
        }

        public static interface IOutputTransformer {
            public ItemStack transform(CraftingContainer inventory, ItemStack staticOutput);
        }
    }
}
