package org.cyclops.cyclopscore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * An extended packet buffer.
 * @author rubensworks
 */
public class ExtendedBuffer extends PacketBuffer {

    public ExtendedBuffer(ByteBuf wrapped) {
        super(wrapped);
    }

    public String readString() {
        return ByteBufUtils.readUTF8String(this);
    }

    @Override
    public ExtendedBuffer writeString(String string) {
        ByteBufUtils.writeUTF8String(this, string);
        return this;
    }

}
