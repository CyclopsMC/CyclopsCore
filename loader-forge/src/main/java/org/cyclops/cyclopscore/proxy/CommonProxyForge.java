package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyForge extends CommonProxyComponentForge {

    @Override
    public ModBaseForge<?> getMod() {
        return CyclopsCoreForge._instance;
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
    }
}
