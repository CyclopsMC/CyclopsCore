package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class ParticleDropColoredConfigComponentClient<M extends IModBase> extends ParticleConfigComponentClient<ParticleDropColoredData, M>  {
    @Nullable
    @Override
    public ParticleProvider<ParticleDropColoredData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleResources.SpriteParticleRegistration<ParticleDropColoredData> getParticleMetaFactory() {
        return sprite -> (particleDropColoredData, world, x, y, z, motionX, motionY, motionZ, randomSource) -> new ParticleDropColored(particleDropColoredData, world, x, y, z, sprite.first());
    }
}
