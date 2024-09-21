package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.event.PlayerRingOfFireFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexSync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketSync;

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
    public void registerEventHooks() {
        super.registerEventHooks();

        new PlayerRingOfFireFabric();
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(RingOfFirePacket.class, RingOfFirePacket.TYPE, RingOfFirePacket.CODEC);
        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);

        // Register debug packets
        packetHandler.register(PingPongPacketAsync.class, PingPongPacketAsync.TYPE, PingPongPacketAsync.CODEC);
        packetHandler.register(PingPongPacketSync.class, PingPongPacketSync.TYPE, PingPongPacketSync.CODEC);
        packetHandler.register(PingPongPacketComplexAsync.class, PingPongPacketComplexAsync.TYPE, PingPongPacketComplexAsync.CODEC);
        packetHandler.register(PingPongPacketComplexSync.class, PingPongPacketComplexSync.TYPE, PingPongPacketComplexSync.CODEC);
    }
}
