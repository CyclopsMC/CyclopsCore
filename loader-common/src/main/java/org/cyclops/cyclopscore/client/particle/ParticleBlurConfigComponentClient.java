package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
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
    public ParticleEngine.SpriteParticleRegistration<ParticleBlurData> getParticleMetaFactory() {
        return sprite -> new ParticleProvider<ParticleBlurData>() {
            @Nullable
            @Override
            public Particle createParticle(ParticleBlurData particleBlurData, ClientLevel world, double x, double y, double z,
                                           double motionX, double motionY, double motionZ) {
                ParticleBlur particle = new ParticleBlur(particleBlurData, world, x, y, z, motionX, motionY, motionZ);
                particle.pickSprite(sprite);
                return particle;
            }
        };
    }
}
