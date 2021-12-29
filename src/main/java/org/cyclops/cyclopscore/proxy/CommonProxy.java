package org.cyclops.cyclopscore.proxy;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.network.packet.*;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexSync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketSync;

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
        packetHandler.register(ButtonClickPacket.class);
        packetHandler.register(ValueNotifyPacket.class);
        packetHandler.register(ReloadResourcesPacket.class);
        packetHandler.register(AdvancementRewardsObtainPacket.class);
        packetHandler.register(RequestPlayerNbtPacket.class);
        packetHandler.register(SendPlayerNbtPacket.class);
        packetHandler.register(RequestPlayerAdvancementUnlockedPacket.class);
        packetHandler.register(SendPlayerAdvancementUnlockedPacket.class);

        // Register debug packets
        packetHandler.register(PingPongPacketAsync.class);
        packetHandler.register(PingPongPacketSync.class);
        packetHandler.register(PingPongPacketComplexAsync.class);
        packetHandler.register(PingPongPacketComplexSync.class);
    }

}
