package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class RecipeConditionConfig<T extends ICondition> extends ExtendedConfig<RecipeConditionConfig<T>, IConditionSerializer<T>> {

    public RecipeConditionConfig(ModBase mod, IConditionSerializer<T> conditionSerializer) {
        super(mod, conditionSerializer.getID().getPath(), (eConfig) -> conditionSerializer);
    }

    @Override
    public String getTranslationKey() {
        return "recipecondition." + getMod().getModId() + "." + getNamedId();
	}

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.RECIPE_CONDITION;
	}

}
