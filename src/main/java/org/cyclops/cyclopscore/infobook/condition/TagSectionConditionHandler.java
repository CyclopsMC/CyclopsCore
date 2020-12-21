package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if a tag key is present.
 * @author rubensworks
 *
 */
public class TagSectionConditionHandler<T> implements ISectionConditionHandler {

	private final ITagCollection<T> tagCollection;

	public TagSectionConditionHandler(ITagCollection<T> tagCollection) {
		this.tagCollection = tagCollection;
	}

	@Override
	public boolean isSatisfied(ModBase<?> mod, String param) {
		ITag<T> collection = this.tagCollection.get(new ResourceLocation(param));
		return collection != null && collection.getAllElements().size() > 0;
	}

}
