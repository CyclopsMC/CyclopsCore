package org.cyclops.cyclopscore.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.RegistryEntries;

import java.util.Locale;

import net.minecraft.core.particles.ParticleOptions.Deserializer;

/**
 * Data for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredData implements ParticleOptions {

    public static final ParticleDropColoredData INSTANCE = new ParticleDropColoredData(0, 0, 0);
    public static final Deserializer<ParticleDropColoredData> DESERIALIZER = new Deserializer<ParticleDropColoredData>() {
        public ParticleDropColoredData fromCommand(ParticleType<ParticleDropColoredData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float red = (float) reader.readDouble();
            reader.expect(' ');
            float green = (float) reader.readDouble();
            reader.expect(' ');
            float blue = (float) reader.readDouble();
            return new ParticleDropColoredData(red, green, blue);
        }

        public ParticleDropColoredData fromNetwork(ParticleType<ParticleDropColoredData> particleTypeIn, FriendlyByteBuf buffer) {
            return new ParticleDropColoredData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };
    public static final Codec<ParticleDropColoredData> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(Codec.FLOAT.fieldOf("r").forGetter(ParticleDropColoredData::getRed),
                    Codec.FLOAT.fieldOf("g").forGetter(ParticleDropColoredData::getGreen),
                    Codec.FLOAT.fieldOf("b").forGetter(ParticleDropColoredData::getBlue))
            .apply(builder, ParticleDropColoredData::new));

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
        return RegistryEntries.PARTICLE_DROP_COLORED;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
                ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
                this.red, this.green, this.blue);
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
