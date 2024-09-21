package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.event.PlayerRingOfFireNeoForge;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.network.packet.AdvancementRewardsObtainPacket;
import org.cyclops.cyclopscore.network.packet.ButtonClickPacket;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;
import org.cyclops.cyclopscore.network.packet.RequestPlayerAdvancementUnlockedPacket;
import org.cyclops.cyclopscore.network.packet.RequestPlayerNbtPacket;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;
import org.cyclops.cyclopscore.network.packet.SendPlayerAdvancementUnlockedPacket;
import org.cyclops.cyclopscore.network.packet.SendPlayerNbtPacket;
import org.cyclops.cyclopscore.network.packet.ValueNotifyPacket;
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
    public void registerEventHooks() {
        super.registerEventHooks();

        NeoForge.EVENT_BUS.register(new PlayerRingOfFireNeoForge());
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);

        // Register packets.
        packetHandler.register(RingOfFirePacket.TYPE, RingOfFirePacket.CODEC);
        packetHandler.register(ButtonClickPacket.TYPE, ButtonClickPacket.CODEC);
        packetHandler.register(ValueNotifyPacket.TYPE, ValueNotifyPacket.CODEC);
        packetHandler.register(ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
        packetHandler.register(AdvancementRewardsObtainPacket.TYPE, AdvancementRewardsObtainPacket.CODEC);
        packetHandler.register(RequestPlayerNbtPacket.TYPE, RequestPlayerNbtPacket.CODEC);
        packetHandler.register(SendPlayerNbtPacket.TYPE, SendPlayerNbtPacket.CODEC);
        packetHandler.register(RequestPlayerAdvancementUnlockedPacket.TYPE, RequestPlayerAdvancementUnlockedPacket.CODEC);
        packetHandler.register(SendPlayerAdvancementUnlockedPacket.TYPE, SendPlayerAdvancementUnlockedPacket.CODEC);

        // Register debug packets
        packetHandler.register(PingPongPacketAsync.TYPE, PingPongPacketAsync.CODEC);
        packetHandler.register(PingPongPacketSync.TYPE, PingPongPacketSync.CODEC);
        packetHandler.register(PingPongPacketComplexAsync.TYPE, PingPongPacketComplexAsync.CODEC);
        packetHandler.register(PingPongPacketComplexSync.TYPE, PingPongPacketComplexSync.CODEC);
    }

}
