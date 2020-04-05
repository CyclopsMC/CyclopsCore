package org.cyclops.cyclopscore.infobook.condition;


import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if configs are enabled.
 * @author rubensworks
 *
 */
public class PredefinedSectionConditionHandler implements ISectionConditionHandler {

	@Override
	public boolean isSatisfied(ModBase<?> mod, String param) {
		//return recipeHandler.isPredefinedValue(param); // TODO: remove class
		return false;
	}

}
