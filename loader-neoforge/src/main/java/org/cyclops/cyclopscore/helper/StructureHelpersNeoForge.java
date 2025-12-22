package org.cyclops.cyclopscore.helper;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

/**
 * @author rubensworks
 */
public class StructureHelpersNeoForge extends StructureHelpersCommon implements IStructureHelpersNeoForge {

    @Override
    public void addToStructureTemplatePool(Identifier structureTemplatePool, Identifier structureTemplate) {
        NeoForge.EVENT_BUS.addListener((TagsUpdatedEvent event) -> {
            if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
                addToStructureTemplatePool(structureTemplatePool, structureTemplate, event.getLookupProvider());
            }
        });
    }
}
