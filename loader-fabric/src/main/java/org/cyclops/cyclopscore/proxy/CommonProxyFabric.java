package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyFabric extends CommonProxyComponentFabric {

    @Override
    public ModBaseFabric<?> getMod() {
        return CyclopsCoreFabric._instance;
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
    }
}
