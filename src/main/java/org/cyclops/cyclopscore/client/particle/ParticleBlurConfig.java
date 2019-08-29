package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfig;

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
        return ParticleBlur::new;
    }

}
