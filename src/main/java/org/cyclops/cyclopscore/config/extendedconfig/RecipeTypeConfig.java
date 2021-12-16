package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for recipe types.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class RecipeTypeConfig<T extends Recipe<?>> extends ExtendedConfig<RecipeTypeConfig<T>, RecipeType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     */
    public RecipeTypeConfig(ModBase mod, String namedId) {
        super(mod, namedId, (eConfig) -> RecipeType.register(mod.getModId() + ":" + namedId));
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
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.RECIPE_TYPE;
	}

}
