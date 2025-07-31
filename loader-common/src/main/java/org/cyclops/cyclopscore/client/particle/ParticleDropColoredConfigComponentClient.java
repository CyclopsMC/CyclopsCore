package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
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
    public ParticleEngine.SpriteParticleRegistration<ParticleDropColoredData> getParticleMetaFactory() {
        return sprite -> (ParticleProvider<ParticleDropColoredData>) (particleDropColoredData, world, x, y, z, motionX, motionY, motionZ) -> {
            ParticleDropColored particle = new ParticleDropColored(particleDropColoredData, world, x, y, z);
            particle.pickSprite(sprite);
            return particle;
        };
    }
}
