package org.cyclops.cyclopscore.config;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Consumer;

/**
 * This is a hack to make Fabric's aproach to passing custom data to guis compatible using StreamCodec's
 * with Forge's and NeoForge's approach that relies on raw FriendlyByteBufs.
 * @author rubensworks
 */
public class StreamCodecConsumerHack<B extends FriendlyByteBuf> implements StreamCodec<B, Boolean> {

    private FriendlyByteBuf buffer;
    private Consumer<FriendlyByteBuf> extraDataWriter;

    @Override
    public Boolean decode(B buffer) {
        // Store a copy of the buffer for later use
        // A copy is needed, as the packet handler will otherwise complain that not all bytes were read.
        FriendlyByteBuf bufferCopy = buffer instanceof RegistryFriendlyByteBuf registryFriendlyByteBuf ?
                new RegistryFriendlyByteBuf(buffer.duplicate(), registryFriendlyByteBuf.registryAccess()) :
                new FriendlyByteBuf(buffer.duplicate());
        bufferCopy.retain();
        this.setBuffer(bufferCopy);

        // Mark the original buffer as fully consumed
        buffer.skipBytes(buffer.readableBytes());

        return true;
    }

    @Override
    public void encode(B buffer, Boolean data) {
        if (this.extraDataWriter != null) {
            this.extraDataWriter.accept(buffer);
            this.extraDataWriter = null;
        }
    }

    public FriendlyByteBuf getAndResetBuffer() {
        FriendlyByteBuf ret = this.buffer;
        this.buffer = null;
        return ret;
    }

    public void setBuffer(FriendlyByteBuf buffer) {
        this.buffer = buffer;
    }

    public void setExtraDataWriter(Consumer<FriendlyByteBuf> extraDataWriter) {
        this.extraDataWriter = extraDataWriter;
    }
}
