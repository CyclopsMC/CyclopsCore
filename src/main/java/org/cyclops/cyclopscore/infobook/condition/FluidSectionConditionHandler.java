package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if a fluid has been registered.
 * @author rubensworks
 *
 */
public class FluidSectionConditionHandler implements ISectionConditionHandler {

	@Override
	public boolean isSatisfied(ModBase<?> mod, String param) {
		return ForgeRegistries.FLUIDS.containsKey(new ResourceLocation(param));
	}

}
