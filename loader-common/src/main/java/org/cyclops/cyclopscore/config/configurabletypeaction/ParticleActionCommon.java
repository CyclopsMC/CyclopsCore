package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link ItemConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class ParticleActionCommon<T extends ParticleOptions, M extends IModBase> extends ConfigurableTypeActionRegistry<ParticleConfigCommon<T, M>, ParticleType<T>, M> {
}
