package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for recipe serializers.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class RecipeConfig<T extends Recipe<?>> extends RecipeConfigCommon<T, ModBase<?>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public RecipeConfig(ModBase<?> mod, String namedId, Function<RecipeConfigCommon<T, ModBase<?>>, ? extends RecipeSerializer<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
