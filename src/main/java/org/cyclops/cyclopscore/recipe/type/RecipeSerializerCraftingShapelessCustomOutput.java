package org.cyclops.cyclopscore.recipe.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Recipe serializer for predefined output items.
 * @author rubensworks
 */
public class RecipeSerializerCraftingShapelessCustomOutput implements RecipeSerializer<RecipeCraftingShapelessCustomOutput> {

    private final Supplier<ItemStack> outputProvider;
    @Nullable
    private final IOutputTransformer outputTransformer;

    public RecipeSerializerCraftingShapelessCustomOutput(Supplier<ItemStack> outputProvider, @Nullable IOutputTransformer outputTransformer) {
        this.outputProvider = outputProvider;
        this.outputTransformer = outputTransformer;
    }

    public RecipeSerializerCraftingShapelessCustomOutput(Supplier<ItemStack> outputProvider) {
        this(outputProvider, null);
    }

    @Nullable
    public IOutputTransformer getOutputTransformer() {
        return outputTransformer;
    }

    // Partially copied from ShapelessRecipe.Serializer

    @Override
    public RecipeCraftingShapelessCustomOutput fromJson(ResourceLocation recipeId, JsonObject json) {
        String s = GsonHelper.getAsString(json, "group", "");
        CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", (String)null), CraftingBookCategory.MISC);
        NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else if (nonnulllist.size() > 3 * 3) {
            throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + (3 * 3));
        } else {
            ItemStack itemstack = this.outputProvider.get(); // This line is different
            return new RecipeCraftingShapelessCustomOutput(this, recipeId, s, craftingbookcategory, itemstack, nonnulllist);
        }
    }

    private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < p_44276_.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i));
            if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }

    @Override
    public RecipeCraftingShapelessCustomOutput fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String s = buffer.readUtf();
        CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
        int i = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

        for(int j = 0; j < nonnulllist.size(); ++j) {
            nonnulllist.set(j, Ingredient.fromNetwork(buffer));
        }

        ItemStack itemstack = buffer.readItem();
        return new RecipeCraftingShapelessCustomOutput(this, recipeId, s, craftingbookcategory, itemstack, nonnulllist);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeCraftingShapelessCustomOutput recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeEnum(recipe.category());
        buffer.writeVarInt(recipe.getIngredients().size());

        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem());
    }

    public static interface IOutputTransformer {
        public ItemStack transform(CraftingContainer inventory, ItemStack staticOutput);
    }
}
