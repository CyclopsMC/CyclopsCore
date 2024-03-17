package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.Reference;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketSync extends PingPongPacketAsync {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "ping_pong_sync");

    /**
     * Empty packet.
     */
    public PingPongPacketSync() {
        super(ID);
    }

    public PingPongPacketSync(int remaining) {
        super(ID, remaining);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketSync(remaining - 1);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

}
