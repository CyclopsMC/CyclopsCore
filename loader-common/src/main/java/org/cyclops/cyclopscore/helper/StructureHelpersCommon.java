package org.cyclops.cyclopscore.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class StructureHelpersCommon implements IStructureHelpers {
    @Override
    public void addToStructureTemplatePool(Identifier structureTemplatePool, Identifier structureTemplate, HolderLookup.Provider registryAccess) {
        HolderLookup.RegistryLookup<StructureTemplatePool> registry = registryAccess.lookupOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool pool = Objects.requireNonNull(registry.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, structureTemplatePool)).value(), structureTemplatePool.getPath());
        if(!(pool.rawTemplates instanceof ArrayList)) {
            pool.rawTemplates = new ArrayList<>(pool.rawTemplates);
        }
        SinglePoolElement addedElement = SinglePoolElement.single(structureTemplate.toString()).apply(StructureTemplatePool.Projection.RIGID);
        pool.rawTemplates.add(Pair.of(addedElement, 1));
        pool.templates.add(addedElement);
    }
}
