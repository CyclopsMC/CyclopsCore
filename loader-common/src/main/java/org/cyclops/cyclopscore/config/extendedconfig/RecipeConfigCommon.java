package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for recipe serializers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class RecipeConfigCommon<T extends Recipe<?>, M extends IModBase> extends ExtendedConfigRegistry<RecipeConfigCommon<T, M>, RecipeSerializer<T>, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public RecipeConfigCommon(M mod, String namedId, Function<RecipeConfigCommon<T, M>, ? extends RecipeSerializer<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "recipe." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.RECIPE;
    }

    @Override
    public Registry<? super RecipeSerializer<T>> getRegistry() {
        return BuiltInRegistries.RECIPE_SERIALIZER;
    }
}
