package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ParticleAction<T extends IParticleData> extends ConfigurableTypeAction<ParticleConfig<T>, ParticleType<T>> {

    @Override
    public void onRegister(ParticleConfig<T> eConfig) {
        System.out.println("Reg particle"); // TODO
        register(eConfig.getInstance(), (ParticleConfig) eConfig);
    }
}
