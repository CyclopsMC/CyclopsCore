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
        packetHandler.register(RingOfFirePacket.ID, RingOfFirePacket::new);
        packetHandler.register(ButtonClickPacket.ID, ButtonClickPacket::new);
        packetHandler.register(ValueNotifyPacket.ID, ValueNotifyPacket::new);
        packetHandler.register(ReloadResourcesPacket.ID, ReloadResourcesPacket::new);
        packetHandler.register(AdvancementRewardsObtainPacket.ID, AdvancementRewardsObtainPacket::new);
        packetHandler.register(RequestPlayerNbtPacket.ID, RequestPlayerNbtPacket::new);
        packetHandler.register(SendPlayerNbtPacket.ID, SendPlayerNbtPacket::new);
        packetHandler.register(RequestPlayerAdvancementUnlockedPacket.ID, RequestPlayerAdvancementUnlockedPacket::new);
        packetHandler.register(SendPlayerAdvancementUnlockedPacket.ID, SendPlayerAdvancementUnlockedPacket::new);

        // Register debug packets
        packetHandler.register(PingPongPacketAsync.ID, PingPongPacketAsync::new);
        packetHandler.register(PingPongPacketSync.ID, PingPongPacketSync::new);
        packetHandler.register(PingPongPacketComplexAsync.ID, PingPongPacketComplexAsync::new);
        packetHandler.register(PingPongPacketComplexSync.ID, PingPongPacketComplexSync::new);
    }

}
