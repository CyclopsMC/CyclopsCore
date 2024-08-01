package org.cyclops.cyclopscore.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.cyclops.cyclopscore.RegistryEntries;

/**
 * Data for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredData implements ParticleOptions {

    public static final ParticleDropColoredData INSTANCE = new ParticleDropColoredData(0, 0, 0);
    public static final MapCodec<ParticleDropColoredData> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder
            .group(Codec.FLOAT.fieldOf("r").forGetter(ParticleDropColoredData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleDropColoredData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleDropColoredData::getBlue))
            .apply(builder, ParticleDropColoredData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleDropColoredData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ParticleDropColoredData::getRed,
            ByteBufCodecs.FLOAT, ParticleDropColoredData::getGreen,
            ByteBufCodecs.FLOAT, ParticleDropColoredData::getBlue,
            ParticleDropColoredData::new
    );

    private final float red;
    private final float green;
    private final float blue;

    public ParticleDropColoredData(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<?> getType() {
        return RegistryEntries.PARTICLE_DROP_COLORED.get();
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
}
