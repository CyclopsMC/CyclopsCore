package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.Reference;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketComplexSync extends PingPongPacketComplexAsync {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "ping_pong_complex_sync");

    /**
     * Empty packet.
     */
    public PingPongPacketComplexSync() {
        super(ID);
    }

    public PingPongPacketComplexSync(int remaining, String string1, String string2) {
        super(ID, remaining, string1, string2);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketComplexSync(remaining - 1, string1, string2);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
