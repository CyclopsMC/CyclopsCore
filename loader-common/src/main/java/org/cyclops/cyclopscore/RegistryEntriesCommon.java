package org.cyclops.cyclopscore;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntriesCommon {

    public static final DeferredHolderCommon<ParticleType<?>, ParticleType<?>> PARTICLE_BLUR = DeferredHolderCommon.create(Registries.PARTICLE_TYPE, ResourceLocation.parse("cyclopscore:blur"));
    public static final DeferredHolderCommon<ParticleType<?>, ParticleType<?>> PARTICLE_DROP_COLORED = DeferredHolderCommon.create(Registries.PARTICLE_TYPE, ResourceLocation.parse("cyclopscore:drop_colored"));

    public static final DeferredHolderCommon<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_CAPACITY = DeferredHolderCommon.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:capacity"));
    public static final DeferredHolderCommon<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_ENERGY_STORAGE = DeferredHolderCommon.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:energy_storage"));

}
