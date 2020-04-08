package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
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
        super(CyclopsCore._instance, "blur", eConfig -> new ParticleType<>(false, ParticleBlurData.DESERIALIZER));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IParticleFactory<ParticleBlurData> getParticleFactory() {
        return null;
    }

    @Nullable
    @Override
    public ParticleManager.IParticleMetaFactory<ParticleBlurData> getParticleMetaFactory() {
        return sprite -> new IParticleFactory<ParticleBlurData>() {
            @Nullable
            @Override
            public Particle makeParticle(ParticleBlurData particleBlurData, World world, double x, double y, double z,
                                         double motionX, double motionY, double motionZ) {
                ParticleBlur particle = new ParticleBlur(particleBlurData, world, x, y, z, motionX, motionY, motionZ);
                particle.selectSpriteRandomly(sprite);
                return particle;
            }
        };
    }

}
