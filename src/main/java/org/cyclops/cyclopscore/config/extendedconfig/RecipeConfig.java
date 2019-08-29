package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
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
public abstract class RecipeConfig<T extends IRecipe<?>> extends ExtendedConfigForge<RecipeConfig<T>, IRecipeSerializer<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public RecipeConfig(ModBase mod, String namedId, Function<RecipeConfig<T>, ? extends IRecipeSerializer<T>> elementConstructor) {
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
    public IForgeRegistry<? super IRecipeSerializer<T>> getRegistry() {
        return ForgeRegistries.RECIPE_SERIALIZERS;
    }

}
