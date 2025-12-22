package org.cyclops.cyclopscore.helper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class StructureHelpersCommon implements IStructureHelpers {
    @Override
    public void addToStructureTemplatePool(ResourceLocation structureTemplatePool, ResourceLocation structureTemplate, RegistryAccess registryAccess) {
        Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registries.TEMPLATE_POOL);
        StructureTemplatePool pool = Objects.requireNonNull(registry.get(structureTemplatePool), structureTemplatePool.getPath());
        if(!(pool.rawTemplates instanceof ArrayList)) {
            pool.rawTemplates = new ArrayList<>(pool.rawTemplates);
        }
        SinglePoolElement addedElement = SinglePoolElement.single(structureTemplate.toString()).apply(StructureTemplatePool.Projection.RIGID);
        pool.rawTemplates.add(Pair.of(addedElement, 1));
        pool.templates.add(addedElement);
    }
}
