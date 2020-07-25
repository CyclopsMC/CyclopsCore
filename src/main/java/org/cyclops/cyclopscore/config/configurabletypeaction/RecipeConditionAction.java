package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.cyclops.cyclopscore.config.extendedconfig.RecipeConditionConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.RecipeConditionConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class RecipeConditionAction<T extends ICondition> extends ConfigurableTypeAction<RecipeConditionConfig<T>, IConditionSerializer<T>> {

    @Override
    public void onRegisterForge(RecipeConditionConfig<T> eConfig) {
        super.onRegisterForge(eConfig);
        CraftingHelper.register(eConfig.getInstance());
    }

}
