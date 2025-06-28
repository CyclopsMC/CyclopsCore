package org.cyclops.cyclopscore.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.event.LecternInfoBookHandlerForge;
import org.cyclops.cyclopscore.event.PlayerRingOfFireForge;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.packet.ButtonClickPacket;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;
import org.cyclops.cyclopscore.network.packet.RingOfFirePacket;
import org.cyclops.cyclopscore.network.packet.ValueNotifyPacket;

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
        PlayerInteractEvent.RightClickBlock.BUS.addListener(LecternInfoBookHandlerForge::onRightClickLectern);
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(RingOfFirePacket.class, RingOfFirePacket.TYPE, RingOfFirePacket.CODEC);
        packetHandler.register(ButtonClickPacket.class, ButtonClickPacket.TYPE, ButtonClickPacket.CODEC);
        packetHandler.register(ValueNotifyPacket.class, ValueNotifyPacket.TYPE, ValueNotifyPacket.CODEC);
        packetHandler.register(ReloadResourcesPacket.class, ReloadResourcesPacket.TYPE, ReloadResourcesPacket.CODEC);
    }
}
