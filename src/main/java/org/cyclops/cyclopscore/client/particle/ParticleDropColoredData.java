package org.cyclops.cyclopscore.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.RegistryEntries;

import java.util.Locale;

/**
 * Data for {@link ParticleDropColored}.
 * @author rubensworks
 */
public class ParticleDropColoredData implements IParticleData {

    public static final ParticleDropColoredData INSTANCE = new ParticleDropColoredData(0, 0, 0);
    public static final IDeserializer<ParticleDropColoredData> DESERIALIZER = new IDeserializer<ParticleDropColoredData>() {
        public ParticleDropColoredData deserialize(ParticleType<ParticleDropColoredData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float red = (float) reader.readDouble();
            reader.expect(' ');
            float green = (float) reader.readDouble();
            reader.expect(' ');
            float blue = (float) reader.readDouble();
            return new ParticleDropColoredData(red, green, blue);
        }

        public ParticleDropColoredData read(ParticleType<ParticleDropColoredData> particleTypeIn, PacketBuffer buffer) {
            return new ParticleDropColoredData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

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
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
    }

    @Override
    public String getParameters() {
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
