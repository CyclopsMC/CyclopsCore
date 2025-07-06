package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

import java.util.function.Supplier;

/**
 * Blood Infuser recipes.
 * @author rubensworks
 */
public class FurnaceRecipeAppendix extends RecipeAppendix<FurnaceRecipeAppendixClient> {

    public static final int SLOT_OFFSET_X = 16;
    public static final int SLOT_OFFSET_Y = 3;
    public static final int START_X_RESULT = 68;

    public static final AdvancedButtonEnum INPUT = AdvancedButtonEnum.create();
    public static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public FurnaceRecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplay) throws InfoBookParser.InvalidAppendixException {
        super(infoBook, recipeDisplay);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 32;
    }

    @Override
    public FurnaceRecipeAppendixClient constructSectionAppendixClient() {
        return new FurnaceRecipeAppendixClient(this);
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
        if (IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            getSectionAppendixClient().bakeElement(infoSection);
        }
        super.bakeElement(infoSection);
    }
}
