package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
// TODO: append NeoForge to name in next major
public class ParticleAction<T extends ParticleOptions> extends ConfigurableTypeActionForge<ParticleConfig<T>, ParticleType<T>> {

}
