package org.cyclops.cyclopscore.proxy;

import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.event.PlayerRingOfFireForge;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;

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
    public void registerEventHooks() {
        super.registerEventHooks();

        MinecraftForge.EVENT_BUS.register(new PlayerRingOfFireForge());
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(RingOfFirePacket.class, RingOfFirePacket.TYPE, RingOfFirePacket.CODEC);
        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
    }
}
