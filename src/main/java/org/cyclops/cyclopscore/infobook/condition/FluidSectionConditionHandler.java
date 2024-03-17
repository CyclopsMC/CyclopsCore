package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if a fluid has been registered.
 * @author rubensworks
 *
 */
public class FluidSectionConditionHandler implements ISectionConditionHandler {

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        return BuiltInRegistries.FLUID.containsKey(new ResourceLocation(param));
    }

}
