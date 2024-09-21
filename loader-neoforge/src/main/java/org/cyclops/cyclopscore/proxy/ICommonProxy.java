package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.PacketHandler;


/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxy extends ICommonProxyCommon {

    /**
     * @return The mod for this proxy.
     */
    public ModBase getMod();

    /**
     * Register key bindings.
     * @param keyRegistry The key registry to register to.
     * @param event Register mappings event
     */
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event);

    /**
     * Register packet handlers.
     * @param packetHandler The packet handler.
     */
    @Deprecated // RM in next major
    public void registerPacketHandlers(PacketHandler packetHandler);

    @Override
    public default void registerPackets(IPacketHandler packetHandler) {
        registerPacketHandlers((PacketHandler) packetHandler);
    }

}
