package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for client-side guis and server-side containers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class RecipeConfig<T extends IRecipe<?>> extends ExtendedConfigForge<RecipeConfig<T>, IRecipeSerializer<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId            A unique name id
     * @param comment            A comment that can be added to the config file line
     * @param elementConstructor The element constructor.
     */
    public RecipeConfig(ModBase mod, boolean enabledDefault, String namedId, String comment,
                        Function<RecipeConfig<T>, ? extends IRecipeSerializer<T>> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "gui." + getMod().getModId() + "." + getNamedId();
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
