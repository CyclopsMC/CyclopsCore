package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.Map;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class FurnaceRecipeAppendix extends RecipeAppendix<IRecipe<IInventory>> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 3;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public FurnaceRecipeAppendix(IInfoBook infoBook, IRecipe<IInventory> recipe) {
        super(infoBook, recipe);
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
    public void drawElementInner(ScreenInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(Lists.newArrayList(recipe.getIngredients().get(0).getMatchingStacks()), tick);
        ItemStack result = prepareItemStack(recipe.getRecipeOutput(), tick);

        // Items
        renderItem(gui, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, x + middle, y + SLOT_OFFSET_Y, new ItemStack(Blocks.FURNACE), mx, my, false, null);
    }
}
