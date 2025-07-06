package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.Map;

/**
 * @author rubensworks
 */
public class FurnaceRecipeAppendixClient extends RecipeAppendixClient<FurnaceRecipeAppendix> {
    protected FurnaceRecipeAppendixClient(FurnaceRecipeAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    public void bakeElement(InfoSection infoSection) {
        Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = getSectionAppendix().getRenderItemHolders();
        renderItemHolders.put(FurnaceRecipeAppendix.INPUT, new ItemButton(getSectionAppendix().getInfoBook()));
        renderItemHolders.put(FurnaceRecipeAppendix.RESULT, new ItemButton(getSectionAppendix().getInfoBook()));
    }

    @Override
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - FurnaceRecipeAppendix.SLOT_SIZE) / 2;
        gui.drawArrowRight(guiGraphics, x + middle - 3, y + FurnaceRecipeAppendix.SLOT_OFFSET_Y + 2);

        // Prepare items
        RecipeDisplayEntry recipeDisplay = getSectionAppendix().getRecipeDisplay();
        if (recipeDisplay == null) {
            return;
        }
        int tick = getTick(gui);
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        ItemStack input = prepareItemStacks(((FurnaceRecipeDisplay) recipeDisplay.display()).ingredient().resolveForStacks(contextMap), tick);
        ItemStack result = prepareItemStacks(recipeDisplay.display().result().resolveForStacks(contextMap), tick);

        // Items
        renderItem(gui, guiGraphics, x + FurnaceRecipeAppendix.SLOT_OFFSET_X, y + FurnaceRecipeAppendix.SLOT_OFFSET_Y, input, mx, my, FurnaceRecipeAppendix.INPUT);
        renderItem(gui, guiGraphics, x + FurnaceRecipeAppendix.START_X_RESULT, y + FurnaceRecipeAppendix.SLOT_OFFSET_Y, result, mx, my, FurnaceRecipeAppendix.RESULT);

        renderItem(gui, guiGraphics, x + middle, y + FurnaceRecipeAppendix.SLOT_OFFSET_Y, new ItemStack(Blocks.FURNACE), mx, my, false, null);
    }
}
