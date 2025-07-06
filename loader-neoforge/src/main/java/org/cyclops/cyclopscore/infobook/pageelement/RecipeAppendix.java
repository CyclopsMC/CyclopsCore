package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.cyclops.cyclopscore.infobook.*;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Recipes that can be added to sections.
 * @author rubensworks
 */
public abstract class RecipeAppendix<C extends RecipeAppendixClient<?>> extends SectionAppendix<C> {

    protected static final int SLOT_SIZE = 16;

    protected Supplier<RecipeDisplayEntry> recipeDisplaySupplier;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = Maps.newHashMap();

    public RecipeAppendix(IInfoBook infoBook, Supplier<RecipeDisplayEntry> recipeDisplaySupplier) throws InfoBookParser.InvalidAppendixException {
        super(infoBook);
        this.recipeDisplaySupplier = recipeDisplaySupplier;
    }

    @Nullable
    public RecipeDisplayEntry getRecipeDisplay() {
        return recipeDisplaySupplier.get();
    }

    public Map<AdvancedButtonEnum, AdvancedButton> getRenderItemHolders() {
        return renderItemHolders;
    }

    @Override
    protected int getHeight() {
        return getHeightInner() + getAdditionalHeight();
    }

    protected abstract int getHeightInner();

    protected int getAdditionalHeight() {
        return 5;
    }

    @Override
    protected int getOffsetY() {
        return getAdditionalHeight();
    }

    protected abstract String getUnlocalizedTitle();

    @Override
    public void preBakeElement(InfoSection infoSection) {
        renderItemHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        infoSection.addAdvancedButtons(getPage(), renderItemHolders.values());
    }

}
