package org.cyclops.cyclopscore.recipe.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer = serializer.getOutputTransformer();
        if (outputTransformer != null) {
            return outputTransformer.transform(inv, this.getResultItem());
        }
        return this.getResultItem().copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
        public final Codec<RecipeCraftingShapedCustomOutput> codec;

        public Serializer(Supplier<ItemStack> outputProvider, @Nullable RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer outputTransformer) {
            this.outputProvider = outputProvider;
            this.outputTransformer = outputTransformer;
            this.codec = RecordCodecBuilder.create(
                    p_311728_ -> p_311728_.group(
                                    ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(p_311729_ -> p_311729_.getGroup()),
                                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(p_311732_ -> p_311732_.category()),
                                    ShapedRecipePattern.MAP_CODEC.forGetter(p_311733_ -> p_311733_.shapedRecipePattern),
                                    // ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(p_311730_ -> p_311730_.result), // This is removed
                                    ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true).forGetter(p_311731_ -> p_311731_.showNotification())
                            )
                            .apply(p_311728_, (group, category, shapedRecipePattern, showNotification) -> new RecipeCraftingShapedCustomOutput(this, group, category, shapedRecipePattern, this.outputProvider.get(), showNotification)) // This line is different
            );
        }

        public Serializer(Supplier<ItemStack> outputProvider) {
            this(outputProvider, null);
        }

        @Nullable
        public RecipeCraftingShapelessCustomOutput.Serializer.IOutputTransformer getOutputTransformer() {
            return outputTransformer;
        }

        @Override
        public Codec<RecipeCraftingShapedCustomOutput> codec() {
            return codec;
        }

        public RecipeCraftingShapedCustomOutput fromNetwork(FriendlyByteBuf p_44240_) {
            String s = p_44240_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44240_.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.fromNetwork(p_44240_);
            ItemStack itemstack = p_44240_.readItem();
            boolean flag = p_44240_.readBoolean();
            return new RecipeCraftingShapedCustomOutput(this, s, craftingbookcategory, shapedrecipepattern, itemstack, flag);
        }

        public void toNetwork(FriendlyByteBuf p_44227_, RecipeCraftingShapedCustomOutput p_44228_) {
            p_44227_.writeUtf(p_44228_.getGroup());
            p_44227_.writeEnum(p_44228_.category());
            p_44228_.shapedRecipePattern.toNetwork(p_44227_);
            p_44227_.writeItem(p_44228_.getResultItem());
            p_44227_.writeBoolean(p_44228_.showNotification());
        }
    }
}
