package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.event.PlayerRingOfFireNeoForge;
import org.cyclops.cyclopscore.event.LecternInfoBookHandlerNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
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
public class CommonProxyNeoForge extends CommonProxyComponent {

    @Override
    public ModBaseNeoForge getMod() {
        return CyclopsCoreNeoForge._instance;
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();

        NeoForge.EVENT_BUS.register(new PlayerRingOfFireNeoForge());
        NeoForge.EVENT_BUS.register(new LecternInfoBookHandlerNeoForge());
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(RingOfFirePacket.class, RingOfFirePacket.TYPE, RingOfFirePacket.CODEC);
        packetHandler.register(ButtonClickPacket.class, ButtonClickPacket.TYPE, ButtonClickPacket.CODEC);
        packetHandler.register(ValueNotifyPacket.class, ValueNotifyPacket.TYPE, ValueNotifyPacket.CODEC);
        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
        packetHandler.register(AdvancementRewardsObtainPacket.class, AdvancementRewardsObtainPacket.TYPE, AdvancementRewardsObtainPacket.CODEC);
        packetHandler.register(RequestPlayerNbtPacket.class, RequestPlayerNbtPacket.TYPE, RequestPlayerNbtPacket.CODEC);
        packetHandler.register(SendPlayerNbtPacket.class, SendPlayerNbtPacket.TYPE, SendPlayerNbtPacket.CODEC);
        packetHandler.register(RequestPlayerAdvancementUnlockedPacket.class, RequestPlayerAdvancementUnlockedPacket.TYPE, RequestPlayerAdvancementUnlockedPacket.CODEC);
        packetHandler.register(SendPlayerAdvancementUnlockedPacket.class, SendPlayerAdvancementUnlockedPacket.TYPE, SendPlayerAdvancementUnlockedPacket.CODEC);
        packetHandler.register(RequestRecipeDisplayPacket.class, RequestRecipeDisplayPacket.TYPE, RequestRecipeDisplayPacket.CODEC);
        packetHandler.register(RequestRecipeDisplaysRegexPacket.class, RequestRecipeDisplaysRegexPacket.TYPE, RequestRecipeDisplaysRegexPacket.CODEC);
        packetHandler.register(SendRecipeDisplayPacket.class, SendRecipeDisplayPacket.TYPE, SendRecipeDisplayPacket.CODEC);
        packetHandler.register(SendRecipeDisplaysRegexDonePacket.class, SendRecipeDisplaysRegexDonePacket.TYPE, SendRecipeDisplaysRegexDonePacket.CODEC);

        // Register debug packets
        packetHandler.register(PingPongPacketAsync.class, PingPongPacketAsync.TYPE, PingPongPacketAsync.CODEC);
        packetHandler.register(PingPongPacketSync.class, PingPongPacketSync.TYPE, PingPongPacketSync.CODEC);
        packetHandler.register(PingPongPacketComplexAsync.class, PingPongPacketComplexAsync.TYPE, PingPongPacketComplexAsync.CODEC);
        packetHandler.register(PingPongPacketComplexSync.class, PingPongPacketComplexSync.TYPE, PingPongPacketComplexSync.CODEC);
    }

}
