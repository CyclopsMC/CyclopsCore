package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleBlurConfigComponentClient<M extends IModBase> extends ParticleConfigComponentClient<ParticleBlurData, M>  {
    @Nullable
    @Override
    public ParticleProvider<ParticleBlurData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<ParticleBlurData> getParticleMetaFactory() {
        return sprite -> (particleBlurData, world, x, y, z, motionX, motionY, motionZ, randomSource) -> new ParticleBlur(particleBlurData, world, x, y, z, motionX, motionY, motionZ, sprite.first());
    }
}
