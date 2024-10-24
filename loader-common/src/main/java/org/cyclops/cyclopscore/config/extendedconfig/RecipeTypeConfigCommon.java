package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;


/**
 * Config for recipe types.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class RecipeTypeConfigCommon<T extends Recipe<?>, M extends IModBase> extends ExtendedConfigRegistry<RecipeTypeConfigCommon<T, M>, RecipeType<T>, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     */
    public RecipeTypeConfigCommon(M mod, String namedId) {
        super(mod, namedId, (eConfig) -> simpleRecipeType(ResourceLocation.fromNamespaceAndPath(mod.getModId(), namedId)));
    }

    @Override
    public String getTranslationKey() {
        return "recipetype." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.RECIPE_TYPE;
    }

    @Override
    public Registry<? super RecipeType<T>> getRegistry() {
        return BuiltInRegistries.RECIPE_TYPE;
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleRecipeType(final ResourceLocation name) {
        final String toString = name.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return toString;
            }
        };
    }
}
