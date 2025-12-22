package org.cyclops.cyclopscore.helper;

import net.minecraft.resources.Identifier;

/**
 * @author rubensworks
 */
public interface IStructureHelpersNeoForge extends IStructureHelpers {

    /**
     * Add the given structure template to a pool.
     * @param structureTemplatePool A pool.
     * @param structureTemplate The template to add.
     */
    public void addToStructureTemplatePool(Identifier structureTemplatePool, Identifier structureTemplate);

}
