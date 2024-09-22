package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.RegistryEntriesCommon;

/**
 * Data for {@link ParticleBlur}.
 * @author rubensworks
 */
public class ParticleBlurData implements ParticleOptions {

    public static final ParticleBlurData INSTANCE = new ParticleBlurData(0, 0, 0, 1, 1);
    public static final MapCodec<ParticleBlurData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(Codec.FLOAT.fieldOf("r").forGetter(ParticleBlurData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleBlurData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleBlurData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleBlurData::getScale),
                    Codec.FLOAT.fieldOf("ageMultiplier").forGetter(ParticleBlurData::getAgeMultiplier))
            .apply(builder, ParticleBlurData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleBlurData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleBlurData::getRed,
            ByteBufCodecs.FLOAT, ParticleBlurData::getGreen,
            ByteBufCodecs.FLOAT, ParticleBlurData::getBlue,
            ByteBufCodecs.FLOAT, ParticleBlurData::getScale,
            ByteBufCodecs.FLOAT, ParticleBlurData::getAgeMultiplier,
            ParticleBlurData::new
    );

    private final float red;
    private final float green;
    private final float blue;
    private final float scale;
    private final float ageMultiplier;

    public ParticleBlurData(float red, float green, float blue,
                            float scale, float ageMultiplier) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.scale = scale;
        this.ageMultiplier = ageMultiplier;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntriesCommon.PARTICLE_BLUR.value();
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getScale() {
        return scale;
    }

    public float getAgeMultiplier() {
        return ageMultiplier;
    }
}
