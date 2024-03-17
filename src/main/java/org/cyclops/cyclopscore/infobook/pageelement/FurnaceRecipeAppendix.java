package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class FurnaceRecipeAppendix extends RecipeAppendix<Recipe<Container>> {

    private static final int SLOT_OFFSET_X = 16;
    private static final int SLOT_OFFSET_Y = 3;
    private static final int START_X_RESULT = 68;

    private static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    private static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public FurnaceRecipeAppendix(IInfoBook infoBook, RecipeHolder<? extends Recipe<Container>> recipe) {
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
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int middle = (width - SLOT_SIZE) / 2;
        gui.drawArrowRight(guiGraphics, x + middle - 3, y + SLOT_OFFSET_Y + 2);

        // Prepare items
        int tick = getTick(gui);
        ItemStack input = prepareItemStacks(Lists.newArrayList(recipe.value().getIngredients().get(0).getItems()), tick);
        ItemStack result = prepareItemStack(recipe.value().getResultItem(Minecraft.getInstance().player.level().registryAccess()), tick);

        // Items
        renderItem(gui, guiGraphics, x + SLOT_OFFSET_X, y + SLOT_OFFSET_Y, input, mx, my, INPUT);
        renderItem(gui, guiGraphics, x + START_X_RESULT, y + SLOT_OFFSET_Y, result, mx, my, RESULT);

        renderItem(gui, guiGraphics, x + middle, y + SLOT_OFFSET_Y, new ItemStack(Blocks.FURNACE), mx, my, false, null);
    }
}
