package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

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
    public boolean isSatisfied(ModBaseNeoForge<?> mod, String param) {
        return this.registry.getTagOrEmpty(TagKey.create(this.registry.key(), ResourceLocation.parse(param))).iterator().hasNext();
    }

}
