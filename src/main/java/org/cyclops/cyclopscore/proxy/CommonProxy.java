package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;
import org.cyclops.cyclopscore.network.packet.SoundPacket;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return CyclopsCore._instance;
    }
 
    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);
    	
    	// Register packets.
        packetHandler.register(RingOfFirePacket.class);
        packetHandler.register(SoundPacket.class);
    }

}
