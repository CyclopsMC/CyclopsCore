package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapedCustomOutput extends ShapedRecipe {

    private final RecipeCraftingShapedCustomOutput.Serializer serializer;
    private final ItemStackTemplate recipeOutput;
    public final ShapedRecipePattern shapedRecipePattern;

    public RecipeCraftingShapedCustomOutput(RecipeCraftingShapedCustomOutput.Serializer serializer, String groupIn, CraftingBookCategory category, ShapedRecipePattern shapedRecipePattern, ItemStackTemplate recipeOutputIn, boolean showNotification) {
        super(new Recipe.CommonInfo(showNotification), new CraftingRecipe.CraftingBookInfo(category, groupIn),
                shapedRecipePattern, recipeOutputIn);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
        this.shapedRecipePattern = shapedRecipePattern;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return (RecipeSerializer<ShapedRecipe>)(Object) this.serializer.getRecipeSerializer();
    }

    @Override
    public ItemStack assemble(CraftingInput inv) {
        RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer = serializer.getOutputTransformer();
        if (outputTransformer != null) {
            return outputTransformer.transform(inv, this.getResultItem());
        }
        return this.getResultItem().copy();
    }

    public ItemStack getResultItem() {
        return this.recipeOutput.create();
    }

    // Partially copied from ShapedRecipe.Serializer
    public static class Serializer {
        private final Supplier<ItemStackTemplate> outputProvider;
        @Nullable
        private final RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer;
        private final RecipeSerializer<RecipeCraftingShapedCustomOutput> recipeSerializer;

        public Serializer(Supplier<ItemStackTemplate> outputProvider, @Nullable RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            MapCodec<RecipeCraftingShapedCustomOutput> codec = RecordCodecBuilder.mapCodec(
                    p_311728_ -> p_311728_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.group()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category()),
                                    ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.shapedRecipePattern),
                                    Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(p_311731_ -> p_311731_.showNotification())
                            )
                            .apply(p_311728_, (group, category, shapedRecipePattern, showNotification) -> new RecipeCraftingShapedCustomOutput(this, group, category, shapedRecipePattern, this.outputProvider.get(), showNotification))
            );
            StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapedCustomOutput> streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
            this.recipeSerializer = new RecipeSerializer<>(codec, streamCodec);
        }

        public Serializer(Supplier<ItemStackTemplate> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        public RecipeSerializer<RecipeCraftingShapedCustomOutput> getRecipeSerializer() {
            return recipeSerializer;
        }

        private RecipeCraftingShapedCustomOutput fromNetwork(RegistryFriendlyByteBuf p_319998_) {
            String s = p_319998_.readUtf();
            CraftingBookCategory craftingbookcategory = p_319998_.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(p_319998_);
            ItemStackTemplate itemstack = ItemStackTemplate.STREAM_CODEC.decode(p_319998_);
            boolean flag = p_319998_.readBoolean();
            return new RecipeCraftingShapedCustomOutput(this, s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
        }

        private void toNetwork(RegistryFriendlyByteBuf p_320738_, RecipeCraftingShapedCustomOutput p_320586_) {
            p_320738_.writeUtf(p_320586_.group());
            p_320738_.writeEnum(p_320586_.category());
            ShapedRecipePattern.STREAM_CODEC.encode(p_320738_, p_320586_.shapedRecipePattern);
            ItemStackTemplate.STREAM_CODEC.encode(p_320738_, p_320586_.recipeOutput);
            p_320738_.writeBoolean(p_320586_.showNotification());
        }
    }
}
