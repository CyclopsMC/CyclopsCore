package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if a tag key is present.
 * @author rubensworks
 *
 */
public class TagSectionConditionHandler<T> implements ISectionConditionHandler {

    private final TagCollection<T> tagCollection;

    public TagSectionConditionHandler(TagCollection<T> tagCollection) {
        this.tagCollection = tagCollection;
    }

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        Tag<T> collection = this.tagCollection.getTag(new ResourceLocation(param));
        return collection != null && collection.getValues().size() > 0;
    }

}
