package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleBlur}.
 * @author rubensworks
 */
public class ParticleBlurConfig extends ParticleConfig<ParticleBlurData> {

    public ParticleBlurConfig() {
        super(CyclopsCore._instance, "blur", eConfig -> new ParticleType<ParticleBlurData>(false) {
            public MapCodec<ParticleBlurData> codec() {
                return ParticleBlurData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBlurData> streamCodec() {
                return ParticleBlurData.STREAM_CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleProvider<ParticleBlurData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
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
