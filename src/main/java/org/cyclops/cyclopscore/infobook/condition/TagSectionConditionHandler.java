package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Optional;

/**
 * Condition handler for checking if a tag key is present.
 * @author rubensworks
 *
 */
public class TagSectionConditionHandler<T> implements ISectionConditionHandler {

    private final Registry<T> registry;

    public TagSectionConditionHandler(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        Optional<HolderSet.Named<T>> collection = this.registry.getTag(TagKey.create(this.registry.key(), new ResourceLocation(param)));
        return collection.isPresent();
    }

}
