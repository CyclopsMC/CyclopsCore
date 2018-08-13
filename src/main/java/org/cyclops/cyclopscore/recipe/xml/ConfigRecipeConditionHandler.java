package org.cyclops.cyclopscore.recipe.xml;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.util.Strings;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class ConfigRecipeConditionHandler implements IRecipeConditionHandler {

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		String modId = null;
		if (param.contains(":")) {
			String[] split = param.split(":");
			modId = split[0];
			param = split[1];
		}
		ModBase mod;
		if (!Strings.isEmpty(modId)) {
			ModContainer untypedMod = Loader.instance().getIndexedModList().get(modId);
			if (!(untypedMod.getMod() instanceof ModBase)) {
				throw new IllegalArgumentException("The mod " + modId + " is not of type ModBase.");
			}
			mod = (ModBase) untypedMod.getMod();
		} else {
			mod = recipeHandler.getMod();
		}
		ExtendedConfig<?> config = mod.getConfigHandler().getDictionary().get(param);
		return config != null && config.isEnabled();
	}

}
