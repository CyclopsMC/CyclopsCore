package org.cyclops.cyclopscore.network;

import java.lang.reflect.Field;

/**
 * @author rubensworks
 */
public interface IPacketCodecRunnable {

    /**
     * Run a type of codec.
     *
     * @param field  The field annotated with {@link CodecField}.
     * @param action The action that must be applied to the field.
     */
    public void run(Field field, PacketCodec.ICodecAction action);

}
