package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.crafting.Recipe;
import org.cyclops.cyclopscore.init.ModBase;


/**
 * Config for recipe types.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class RecipeTypeConfig<T extends Recipe<?>> extends RecipeTypeConfigCommon<T, ModBase<?>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     */
    public RecipeTypeConfig(ModBase<?> mod, String namedId) {
        super(mod, namedId);
    }
}
