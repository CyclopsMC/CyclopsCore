package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if a tag key is present.
 * @author rubensworks
 *
 */
public class TagSectionConditionHandler<T extends IForgeRegistryEntry<T>> implements ISectionConditionHandler {

    private final ResourceKey<? extends Registry<T>> registry;
    private final ITagManager<T> tagCollection;

    public TagSectionConditionHandler(ITagManager<T> tagCollection, ResourceKey<? extends Registry<T>> registry) {
        this.tagCollection = tagCollection;
        this.registry = registry;
    }

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        ITag<T> collection = this.tagCollection.getTag(TagKey.create(this.registry, new ResourceLocation(param)));
        return collection.size() > 0;
    }

}
