package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for {@link ParticleBlur}.
 * @author rubensworks
 */
public class ParticleBlurConfig<M extends IModBase> extends ParticleConfigCommon<ParticleBlurData, M> {

    public ParticleBlurConfig(M mod) {
        super(mod, "blur", eConfig -> new ParticleType<>(false) {
            public MapCodec<ParticleBlurData> codec() {
                return ParticleBlurData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleBlurData> streamCodec() {
                return ParticleBlurData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleBlurData, M> getClientComponent() {
        return new ParticleBlurConfigComponentClient<>();
    }

}
