package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.List;
import java.util.function.Supplier;

/**
 * Shaped recipes.
 * @author rubensworks
 */
public class CraftingRecipeAppendix extends RecipeAppendix<CraftingRecipe> {

    private static final int SLOT_OFFSET_X = 5;
    private static final int SLOT_OFFSET_Y = 5;
    private static final int START_X_RESULT = 84;

    private static final AdvancedButtonEnum[] INPUT = new AdvancedButtonEnum[9];
    static {
        for(int i = 0; i < 9; i++) INPUT[i] = AdvancedButtonEnum.create();
    }
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public CraftingRecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplay) {
        super(infoBook, recipeDisplay);
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
        RecipeDisplayEntry recipeDisplay = getRecipeDisplay();
        if (recipeDisplay == null) {
            return;
        }
        int tick = getTick(gui);
        ItemStack[] grid = new ItemStack[9];
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        ItemStack result = prepareItemStacks(recipeDisplay.display().result().resolveForStacks(contextMap), tick);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[i + j * 3] = getItemStacks(recipeDisplay, i + j * 3, contextMap, tick);
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
    private static NonNullList<ItemStack> formatShapedGrid(List<ItemStack> itemStacksRaw, int width, int height) {
        int rawIndex = 0;
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(9, ItemStack.EMPTY);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                itemStacks.set(y * 3 + x, itemStacksRaw.get(rawIndex++));
                if(rawIndex >= itemStacksRaw.size()) break;
            }
            if(rawIndex >= itemStacksRaw.size()) break;
        }
        return itemStacks;
    }

    protected ItemStack getItemStacks(RecipeDisplayEntry recipeDisplay, int index, ContextMap contextMap, int tick) {
        List<ItemStack> ingredients;

        if (recipeDisplay.display() instanceof ShapedCraftingRecipeDisplay shapedDisplay) {
            ingredients = formatShapedGrid(shapedDisplay.ingredients().stream()
                            .map(display -> prepareItemStacks(display.resolveForStacks(contextMap), tick))
                            .toList(),
                    shapedDisplay.width(), shapedDisplay.height());
        } else if (recipeDisplay.display() instanceof ShapelessCraftingRecipeDisplay shapelessDisplay) {
            ingredients = shapelessDisplay.ingredients().stream()
                    .map(display -> prepareItemStacks(display.resolveForStacks(contextMap), tick))
                    .toList();
        } else {
            throw new IllegalArgumentException("Unsupported recipe display class: " + recipeDisplay.display());
        }
        if(ingredients.size() <= index) return ItemStack.EMPTY;
        return ingredients.get(index);
    }

}
