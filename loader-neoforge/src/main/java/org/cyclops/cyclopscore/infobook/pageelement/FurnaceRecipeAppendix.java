package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.function.Supplier;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class FurnaceRecipeAppendix extends RecipeAppendix<SmeltingRecipe> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 3;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public FurnaceRecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplay) {
        super(infoBook, recipeDisplay);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    protected int getHeightInner() {
        return 22;
    }

    @Override
    protected String getUnlocalizedTitle() {
        return "block.minecraft.furnace";
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderItemHolders.put(INPUT, new ItemButton(getInfoBook()));
        renderItemHolders.put(RESULT, new ItemButton(getInfoBook()));
        super.bakeElement(infoSection);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(guiGraphics, x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        RecipeDisplayEntry recipeDisplay = getRecipeDisplay();
        if (recipeDisplay == null) {
            return;
        }
        int tick = getTick(gui);
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        ItemStack input = prepareItemStacks(((FurnaceRecipeDisplay) recipeDisplay.display()).ingredient().resolveForStacks(contextMap), tick);
        ItemStack result = prepareItemStacks(recipeDisplay.display().result().resolveForStacks(contextMap), tick);

        // Items
        renderItem(gui, guiGraphics, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, guiGraphics, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, guiGraphics, x + middle, y + SLOT_OFFSET_Y, new ItemStack(Blocks.FURNACE), mx, my, false, null);
    }
}
