package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
            public Codec<ParticleDropColoredData> func_230522_e_() {
                return ParticleDropColoredData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleDropColoredData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleDropColoredData> getParticleMetaFactory() {
        return sprite -> new IParticleFactory<ParticleDropColoredData>() {
            @Nullable
            @Override
            public Particle makeParticle(ParticleDropColoredData particleDropColoredData, ClientWorld world, double x, double y, double z,
                                         double motionX, double motionY, double motionZ) {
                ParticleDropColored particle = new ParticleDropColored(particleDropColoredData, world, x, y, z);
                particle.selectSpriteRandomly(sprite);
                return particle;
            }
        };
    }

}
