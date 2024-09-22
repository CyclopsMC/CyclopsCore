package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredConfig<M extends IModBase> extends ParticleConfigCommon<ParticleDropColoredData, M> {

    public ParticleDropColoredConfig(M mod) {
        super(mod, "drop_colored", eConfig -> new ParticleType<>(false){
            public MapCodec<ParticleDropColoredData> codec() {
                return ParticleDropColoredData.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ParticleDropColoredData> streamCodec() {
                return ParticleDropColoredData.STREAM_CODEC;
            }
        });
    }

    @Override
    public ParticleConfigComponentClient<ParticleDropColoredData, M> getClientComponent() {
        return new ParticleDropColoredConfigComponentClient<>();
    }
}
