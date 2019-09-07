package org.cyclops.cyclopscore.recipe.xml;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.init.RecipeHandler;

/**
 * Condition handler for checking if a tag key is present.
 * @author rubensworks
 *
 */
public class TagConditionHandler<T> implements IRecipeConditionHandler {

	private final TagCollection<T> tagCollection;

	public TagConditionHandler(TagCollection<T> tagCollection) {
		this.tagCollection = tagCollection;
	}

	@Override
	public boolean isSatisfied(RecipeHandler recipeHandler, String param) {
		Tag<T> collection = this.tagCollection.get(new ResourceLocation(param));
		return collection != null && collection.getAllElements().size() > 0;
	}

}
