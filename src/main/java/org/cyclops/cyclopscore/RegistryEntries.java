package org.cyclops.cyclopscore;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<ParticleType<?>, ParticleType<?>> PARTICLE_BLUR = DeferredHolder.create(Registries.PARTICLE_TYPE, new ResourceLocation("cyclopscore:blur"));
    public static final DeferredHolder<ParticleType<?>, ParticleType<?>> PARTICLE_DROP_COLORED = DeferredHolder.create(Registries.PARTICLE_TYPE, new ResourceLocation("cyclopscore:drop_colored"));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerInfoBookTest>> CONTAINER_INFOBOOK_TEST = DeferredHolder.create(Registries.MENU, new ResourceLocation("cyclopscore:test_infobook"));

}
