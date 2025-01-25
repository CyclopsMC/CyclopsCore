package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for recipe display types.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class RecipeDisplayConfigCommon<T extends RecipeDisplay, M extends IModBase> extends ExtendedConfigRegistry<RecipeDisplayConfigCommon<T, M>, RecipeDisplay.Type<T>, M> {

    public RecipeDisplayConfigCommon(M mod, String namedId, Function<RecipeDisplayConfigCommon<T, M>, RecipeDisplay.Type<T>> factory) {
        super(mod, namedId, factory);
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
        return ConfigurableTypeCommon.RECIPE_DISPLAY;
    }

    @Override
    public Registry<? super RecipeDisplay.Type<T>> getRegistry() {
        return BuiltInRegistries.RECIPE_DISPLAY;
    }
}
