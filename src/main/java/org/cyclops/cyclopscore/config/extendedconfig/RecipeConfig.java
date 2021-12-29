package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for recipe serializers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class RecipeConfig<T extends Recipe<?>> extends ExtendedConfigForge<RecipeConfig<T>, RecipeSerializer<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public RecipeConfig(ModBase mod, String namedId, Function<RecipeConfig<T>, ? extends RecipeSerializer<T>> elementConstructor) {
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
    public IForgeRegistry<? super RecipeSerializer<T>> getRegistry() {
        return ForgeRegistries.RECIPE_SERIALIZERS;
    }

}
