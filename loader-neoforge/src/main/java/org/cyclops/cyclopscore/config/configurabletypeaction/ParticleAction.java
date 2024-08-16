package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
// TODO: append NeoForge to name in next major
public class ParticleAction<T extends ParticleOptions, M extends ModBase> extends ConfigurableTypeActionRegistry<ParticleConfig<T, M>, ParticleType<T>, M> {

}
