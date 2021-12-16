package org.cyclops.cyclopscore.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.RegistryEntries;

import java.util.Locale;

/**
 * Data for {@link ParticleBlur}.
 * @author rubensworks
 */
public class ParticleBlurData implements ParticleOptions {

    public static final ParticleBlurData INSTANCE = new ParticleBlurData(0, 0, 0, 1, 1);
    public static final ParticleOptions.Deserializer<ParticleBlurData> DESERIALIZER = new ParticleOptions.Deserializer<ParticleBlurData>() {
        public ParticleBlurData fromCommand(ParticleType<ParticleBlurData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float red = (float) reader.readDouble();
            reader.expect(' ');
            float green = (float) reader.readDouble();
            reader.expect(' ');
            float blue = (float) reader.readDouble();
            reader.expect(' ');
            float scale = (float) reader.readDouble();
            reader.expect(' ');
            float ageMultiplier = (float) reader.readDouble();
            return new ParticleBlurData(red, green, blue, scale, ageMultiplier);
        }

        public ParticleBlurData fromNetwork(ParticleType<ParticleBlurData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleBlurData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                    buffer.readFloat(), buffer.readFloat());
        }
    };
    public static final Codec<ParticleBlurData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(Codec.FLOAT.fieldOf("r").forGetter(ParticleBlurData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleBlurData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleBlurData::getBlue),
                    Codec.FLOAT.fieldOf("scale").forGetter(ParticleBlurData::getScale),
                    Codec.FLOAT.fieldOf("ageMultiplier").forGetter(ParticleBlurData::getAgeMultiplier))
            .apply(builder, ParticleBlurData::new));

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
        return RegistryEntries.PARTICLE_BLUR;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(this.ageMultiplier);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.red, this.green, this.blue,
                this.scale, this.ageMultiplier);
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
