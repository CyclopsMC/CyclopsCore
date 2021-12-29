package org.cyclops.cyclopscore.network.packet.debug;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketComplexSync extends PingPongPacketComplexAsync {

    /**
     * Empty packet.
     */
    public PingPongPacketComplexSync() {
        super();
    }

    public PingPongPacketComplexSync(int remaining, String string1, String string2) {
        super(remaining, string1, string2);
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketComplexSync(remaining - 1, string1, string2);
    }

    @Override
    public boolean isAsync() {
        return true;
    }
}
