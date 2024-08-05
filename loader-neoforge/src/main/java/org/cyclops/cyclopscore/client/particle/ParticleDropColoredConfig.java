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
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredConfig extends ParticleConfig<ParticleDropColoredData, ModBase<?>> {

    public ParticleDropColoredConfig() {
        super(CyclopsCore._instance, "drop_colored", eConfig -> new ParticleType<>(false){
            public MapCodec<ParticleDropColoredData> codec() {
                return ParticleDropColoredData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDropColoredData> streamCodec() {
                return ParticleDropColoredData.STREAM_CODEC;
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
