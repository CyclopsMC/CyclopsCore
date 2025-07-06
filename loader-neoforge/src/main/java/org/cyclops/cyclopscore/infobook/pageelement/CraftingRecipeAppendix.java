package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

import java.util.function.Supplier;

/**
 * Shaped recipes.
 * @author rubensworks
 */
public class CraftingRecipeAppendix extends RecipeAppendix<CraftingRecipeAppendixClient> {

    public static final int SLOT_OFFSET_X = 5;
    public static final int SLOT_OFFSET_Y = 5;
    public static final int START_X_RESULT = 84;

    public static final AdvancedButtonEnum[] INPUT = new AdvancedButtonEnum[9];
    static {
        for(int i = 0; i < 9; i++) INPUT[i] = AdvancedButtonEnum.create();
    }
    public static final AdvancedButtonEnum RESULT = AdvancedButtonEnum.create();

    public CraftingRecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplay) throws InfoBookParser.InvalidAppendixException {
        super(infoBook, recipeDisplay);
    }

    @Override
    protected int getWidth() {
        return START_X_RESULT + 20;
    }

    @Override
    public CraftingRecipeAppendixClient constructSectionAppendixClient() {
        return new CraftingRecipeAppendixClient(this);
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
        if (IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            getSectionAppendixClient().bakeElement(infoSection);
        }
        super.bakeElement(infoSection);
    }

}
