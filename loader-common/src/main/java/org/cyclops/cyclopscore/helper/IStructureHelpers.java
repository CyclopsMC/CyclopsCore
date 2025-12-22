package org.cyclops.cyclopscore.helper;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;

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
    public void addToStructureTemplatePool(Identifier structureTemplatePool, Identifier structureTemplate, HolderLookup.Provider registryAccess);

}
