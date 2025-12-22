package org.cyclops.cyclopscore.helper;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

/**
 * @author rubensworks
 */
public interface IStructureHelpers {

    /**
     * Add the given structure template to a pool.
     * @param structureTemplatePool A pool.
     * @param structureTemplate The template to add.
     * @param registryAccess A registry access.
     */
    public void addToStructureTemplatePool(ResourceLocation structureTemplatePool, ResourceLocation structureTemplate, RegistryAccess registryAccess);

}
