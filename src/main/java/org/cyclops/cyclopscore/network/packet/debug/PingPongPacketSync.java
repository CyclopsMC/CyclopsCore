package org.cyclops.cyclopscore.network.packet.debug;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketSync extends PingPongPacketAsync {

    /**
     * Empty packet.
     */
    public PingPongPacketSync() {
        super();
    }

    public PingPongPacketSync(int remaining) {
        super(remaining);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketSync(remaining - 1);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

}
