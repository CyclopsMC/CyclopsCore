package org.cyclops.cyclopscore;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;
import org.cyclops.cyclopscore.inventory.SimpleInventory;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<ParticleType<?>, ParticleType<?>> PARTICLE_BLUR = DeferredHolder.create(Registries.PARTICLE_TYPE, ResourceLocation.parse("cyclopscore:blur"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<?>> PARTICLE_DROP_COLORED = DeferredHolder.create(Registries.PARTICLE_TYPE, ResourceLocation.parse("cyclopscore:drop_colored"));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerInfoBookTest>> CONTAINER_INFOBOOK_TEST = DeferredHolder.create(Registries.MENU, ResourceLocation.parse("cyclopscore:test_infobook"));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_CAPACITY = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:capacity"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COMPONENT_ENERGY_STORAGE = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:energy_storage"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> COMPONENT_FLUID_CONTENT = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:fluid_content"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleInventory>> COMPONENT_INVENTORY = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, ResourceLocation.parse("cyclopscore:inventory"));

}
