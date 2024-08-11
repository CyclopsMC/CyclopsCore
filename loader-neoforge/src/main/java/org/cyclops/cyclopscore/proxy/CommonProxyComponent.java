package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.event.PlayerRingOfFire;
import org.cyclops.cyclopscore.network.PacketHandler;

/**
 * Base proxy for server and client side.
 * @author rubensworks
 *
 */
public abstract class CommonProxyComponent extends CommonProxyComponentCommon implements ICommonProxy {

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event) {
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {

    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        NeoForge.EVENT_BUS.register(new PlayerRingOfFire());
    }
}