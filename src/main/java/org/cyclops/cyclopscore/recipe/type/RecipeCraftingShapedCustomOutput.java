package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class RecipeCraftingShapedCustomOutput extends ShapedRecipe {

    private final RecipeCraftingShapedCustomOutput.Serializer serializer;
    private final ItemStack recipeOutput;
    private final ShapedRecipePattern shapedRecipePattern;

    public RecipeCraftingShapedCustomOutput(RecipeCraftingShapedCustomOutput.Serializer serializer, String groupIn, CraftingBookCategory category, ShapedRecipePattern shapedRecipePattern, ItemStack recipeOutputIn, boolean showNotification) {
        super(groupIn, category, shapedRecipePattern, recipeOutputIn, showNotification);
        this.serializer = serializer;
        this.recipeOutput = recipeOutputIn;
        this.shapedRecipePattern = shapedRecipePattern;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer = serializer.getOutputTransformer();
        if (outputTransformer != null) {
            return outputTransformer.transform(inv, this.getResultItem());
        }
        return this.getResultItem().copy();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return getResultItem();
    }

    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    // Partially copied from ShapedRecipe.Serializer
    public static class Serializer implements RecipeSerializer<RecipeCraftingShapedCustomOutput> {
        private final Supplier<ItemStack> outputProvider;
        @Nullable
        private final RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer;
        public final MapCodec<RecipeCraftingShapedCustomOutput> codec;
        public final StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapedCustomOutput> streamCodec;

        public Serializer(Supplier<ItemStack> outputProvider, @Nullable RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            this.codec = RecordCodecBuilder.mapCodec(
                    p_311728_ -> p_311728_.group(
                                    Codec.STRING.optionalFieldOf("group", "").forGetter(p_311729_ -> p_311729_.getGroup()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category()),
                                    ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.shapedRecipePattern),
                                    // ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.result), // This is removed
                                    Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(p_311731_ -> p_311731_.showNotification())
                            )
                            .apply(p_311728_, (group, category, shapedRecipePattern, showNotification) -> new RecipeCraftingShapedCustomOutput(this, group, category, shapedRecipePattern, this.outputProvider.get(), showNotification)) // This line is different
            );
            this.streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
        }

        public Serializer(Supplier<ItemStack> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        @Override
        public MapCodec<RecipeCraftingShapedCustomOutput> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RecipeCraftingShapedCustomOutput> streamCodec() {
            return streamCodec;
        }

        private RecipeCraftingShapedCustomOutput fromNetwork(RegistryFriendlyByteBuf p_319998_) {
            String s = p_319998_.readUtf();
            CraftingBookCategory craftingbookcategory = p_319998_.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(p_319998_);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(p_319998_);
            boolean flag = p_319998_.readBoolean();
            return new RecipeCraftingShapedCustomOutput(this, s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
        }

        private void toNetwork(RegistryFriendlyByteBuf p_320738_, RecipeCraftingShapedCustomOutput p_320586_) {
            p_320738_.writeUtf(p_320586_.getGroup());
            p_320738_.writeEnum(p_320586_.category());
            ShapedRecipePattern.STREAM_CODEC.encode(p_320738_, p_320586_.shapedRecipePattern);
            ItemStack.STREAM_CODEC.encode(p_320738_, p_320586_.getResultItem());
            p_320738_.writeBoolean(p_320586_.showNotification());
        }
    }
}
