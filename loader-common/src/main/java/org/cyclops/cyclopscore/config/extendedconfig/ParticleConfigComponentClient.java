package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * Client-side config component for particle effects.
 * @author rubensworks
 */
public abstract class ParticleConfigComponentClient<T extends ParticleOptions, M extends IModBase> {

    @Nullable
    public abstract ParticleProvider<T> getParticleFactory();

    @Nullable
    public abstract ParticleEngine.SpriteParticleRegistration<T> getParticleMetaFactory();

}
