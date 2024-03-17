package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * Shaped recipes.
 * @author rubensworks
 */
public class CraftingRecipeAppendix extends RecipeAppendix<Recipe<CraftingContainer>> {

    private static final int SLOT_OFFSET_X = 5;
    private static final int SLOT_OFFSET_Y = 5;
    private static final int START_X_RESULT = 84;

    private static final AdvancedButtonEnum[] INPUT = new AdvancedButtonEnum[9];
    static {
        for(int i = 0; i < 9; i++) INPUT[i] = AdvancedButtonEnum.create();
    }
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public CraftingRecipeAppendix(IInfoBook infoBook, RecipeHolder<? extends Recipe<CraftingContainer>> recipe) {
        super(infoBook, recipe);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 20;
    }

    @Override
    protected int getHeightInner() {
        return 58;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "block.minecraft.crafting_table";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        for(int i = 0; i < 9; i++) renderItemHolders.put(INPUT[i], new ItemButton(getInfoBook()));
        renderItemHolders.put(RESULT, new ItemButton(getInfoBook()));
        super.bakeElement(infoSection);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawArrowRight(guiGraphics, x + (SLOT_SIZE + SLOT_OFFSET_X) * 3 - 3, y + SLOT_OFFSET_Y + SLOT_SIZE + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack[] grid = new ItemStack[9];
        ItemStack result = prepareItemStack(recipe.value().getResultItem(Minecraft.getInstance().player.level().registryAccess()), tick);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[i + j * 3] = prepareItemStacks(Lists.newArrayList(getItemStacks(i + j * 3).getItems()), tick);
            }
        }

        // Items
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                renderItem(gui, guiGraphics, x + (SLOT_SIZE + SLOT_OFFSET_X) * i, y+ (SLOT_SIZE + SLOT_OFFSET_Y) * j,
                        grid[i + j * 3], mx, my, INPUT[i + j * 3]);
            }
        }
        renderItem(gui, guiGraphics, x + START_X_RESULT, y + (SLOT_SIZE + SLOT_OFFSET_Y), result, mx, my, RESULT);

        // Crafting Table icon
        renderItem(gui, guiGraphics, x + (SLOT_SIZE + SLOT_OFFSET_X) * 3, y + SLOT_OFFSET_Y + SLOT_SIZE,
                new ItemStack(Blocks.CRAFTING_TABLE), mx, my, false, null);
    }

    /**
     * This method makes sure that recipes which do not take up a full 3x3 crafting grid are still returned inside a
     * 3x3 object array formatted like if they were in a full grid.
     * @param itemStacksRaw An array of items with length width * height
     * @param width The original recipe width.
     * @param height The original recipe height.
     * @return The reformatted object array.
     */
    private static NonNullList<Ingredient> formatShapedGrid(NonNullList<Ingredient> itemStacksRaw, int width, int height) {
        int rawIndex = 0;
        NonNullList<Ingredient> itemStacks = NonNullList.withSize(9, Ingredient.EMPTY);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                itemStacks.set(y * 3 + x, itemStacksRaw.get(rawIndex++));
                if(rawIndex >= itemStacksRaw.size()) break;
            }
            if(rawIndex >= itemStacksRaw.size()) break;
        }
        return itemStacks;
    }

    protected Ingredient getItemStacks(int index) {
        NonNullList<Ingredient> ingredients;

        if(recipe.value() instanceof ShapedRecipe) {
            ingredients = formatShapedGrid(recipe.value().getIngredients(),
                    ((ShapedRecipe) recipe.value()).getRecipeWidth(), ((ShapedRecipe) recipe.value()).getRecipeHeight());
        } else {
            ingredients = recipe.value().getIngredients();
        }
        if(ingredients.size() <= index) return Ingredient.EMPTY;
        return ingredients.get(index);
    }

}
