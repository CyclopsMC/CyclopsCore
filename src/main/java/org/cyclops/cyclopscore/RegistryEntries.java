package org.cyclops.cyclopscore;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder(registryName = "particle_type", value = "cyclopscore:blur")
    public static final ParticleType<?> PARTICLE_BLUR = null;
    @ObjectHolder(registryName = "particle_type", value = "cyclopscore:drop_colored")
    public static final ParticleType<?> PARTICLE_DROP_COLORED = null;

    @ObjectHolder(registryName = "particle_type", value = "cyclopscore:test_infobook")
    public static final MenuType<ContainerInfoBookTest> CONTAINER_INFOBOOK_TEST = null;

}
