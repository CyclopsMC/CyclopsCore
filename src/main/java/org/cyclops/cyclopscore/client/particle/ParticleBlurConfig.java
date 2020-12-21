package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

import javax.annotation.Nullable;

/**
 * Config for {@link ParticleBlur}.
 * @author rubensworks
 */
public class ParticleBlurConfig extends ParticleConfig<ParticleBlurData> {

    public ParticleBlurConfig() {
        super(CyclopsCore._instance, "blur", eConfig -> new ParticleType<ParticleBlurData>(false, ParticleBlurData.DESERIALIZER) {
            public Codec<ParticleBlurData> func_230522_e_() {
                return ParticleBlurData.CODEC;
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public IParticleFactory<ParticleBlurData> getParticleFactory() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBlurData> getParticleMetaFactory() {
        return sprite -> new IParticleFactory<ParticleBlurData>() {
            @Nullable
            @Override
            public Particle makeParticle(ParticleBlurData particleBlurData, ClientWorld world, double x, double y, double z,
                                         double motionX, double motionY, double motionZ) {
                ParticleBlur particle = new ParticleBlur(particleBlurData, world, x, y, z, motionX, motionY, motionZ);
                particle.selectSpriteRandomly(sprite);
                return particle;
            }
        };
    }

}
