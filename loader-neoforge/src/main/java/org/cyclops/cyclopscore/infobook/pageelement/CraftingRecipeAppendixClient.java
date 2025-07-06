package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.List;
import java.util.Map;

/**
 * @author rubensworks
 */
public class CraftingRecipeAppendixClient extends RecipeAppendixClient<CraftingRecipeAppendix> {
    protected CraftingRecipeAppendixClient(CraftingRecipeAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    public void bakeElement(InfoSection infoSection) {
        Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = getSectionAppendix().getRenderItemHolders();
        for(int i = 0; i < 9; i++) renderItemHolders.put(CraftingRecipeAppendix.INPUT[i], new ItemButton(getSectionAppendix().getInfoBook()));
        renderItemHolders.put(CraftingRecipeAppendix.RESULT, new ItemButton(getSectionAppendix().getInfoBook()));
    }

    @Override
    protected void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawArrowRight(guiGraphics, x + (CraftingRecipeAppendix.SLOT_SIZE + CraftingRecipeAppendix.SLOT_OFFSET_X) * 3 - 3, y + CraftingRecipeAppendix.SLOT_OFFSET_Y + CraftingRecipeAppendix.SLOT_SIZE + 2);

        // Prepare items
        RecipeDisplayEntry recipeDisplay = getSectionAppendix().getRecipeDisplay();
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
                renderItem(gui, guiGraphics, x + (CraftingRecipeAppendix.SLOT_SIZE + CraftingRecipeAppendix.SLOT_OFFSET_X) * i, y+ (CraftingRecipeAppendix.SLOT_SIZE + CraftingRecipeAppendix.SLOT_OFFSET_Y) * j,
                        grid[i + j * 3], mx, my, CraftingRecipeAppendix.INPUT[i + j * 3]);
            }
        }
        renderItem(gui, guiGraphics, x + CraftingRecipeAppendix.START_X_RESULT, y + (CraftingRecipeAppendix.SLOT_SIZE + CraftingRecipeAppendix.SLOT_OFFSET_Y), result, mx, my, CraftingRecipeAppendix.RESULT);

        // Crafting Table icon
        renderItem(gui, guiGraphics, x + (CraftingRecipeAppendix.SLOT_SIZE + CraftingRecipeAppendix.SLOT_OFFSET_X) * 3, y + CraftingRecipeAppendix.SLOT_OFFSET_Y + CraftingRecipeAppendix.SLOT_SIZE,
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
