package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredConfig extends ParticleConfig<ParticleDropColoredData> {

    public ParticleDropColoredConfig() {
        super(CyclopsCore._instance, "drop_colored", eConfig -> new ParticleType<ParticleDropColoredData>(false, ParticleDropColoredData.DESERIALIZER){
            public Codec<ParticleDropColoredData> codec() {
                return ParticleDropColoredData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleDropColoredData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleEngine.SpriteParticleRegistration<ParticleDropColoredData> getParticleMetaFactory() {
        return sprite -> new ParticleProvider<ParticleDropColoredData>() {
            @Nullable
            @Override
            public Particle createParticle(ParticleDropColoredData particleDropColoredData, ClientLevel world, double x, double y, double z,
                                         double motionX, double motionY, double motionZ) {
                ParticleDropColored particle = new ParticleDropColored(particleDropColoredData, world, x, y, z);
                particle.pickSprite(sprite);
                return particle;
            }
        };
    }

}
