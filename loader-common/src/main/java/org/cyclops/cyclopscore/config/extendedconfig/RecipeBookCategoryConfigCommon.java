package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for recipe book category types.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class RecipeBookCategoryConfigCommon<T extends RecipeBookCategory, M extends IModBase> extends ExtendedConfigRegistry<RecipeBookCategoryConfigCommon<T, M>, T, M> {

    public RecipeBookCategoryConfigCommon(M mod, String namedId, Function<RecipeBookCategoryConfigCommon<T, M>, T> factory) {
        super(mod, namedId, factory);
    }

    public static <M extends IModBase> Function<RecipeBookCategoryConfigCommon<RecipeBookCategory, M>, RecipeBookCategory> createDefault() {
        return eConfig -> new RecipeBookCategory();
    }

    @Override
    public String getTranslationKey() {
        return "trunkplacer." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.RECIPE_BOOK_CATEGORY;
    }

    @Override
    public Registry<? super T> getRegistry() {
        return BuiltInRegistries.RECIPE_BOOK_CATEGORY;
    }
}
