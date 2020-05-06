package org.cyclops.cyclopscore;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.cyclopscore.infobook.test.ContainerInfoBookTest;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder("cyclopscore:blur")
    public static final ParticleType<?> PARTICLE_BLUR = null;

    @ObjectHolder("cyclopscore:test_infobook")
    public static final ContainerType<ContainerInfoBookTest> CONTAINER_INFOBOOK_TEST = null;

}
