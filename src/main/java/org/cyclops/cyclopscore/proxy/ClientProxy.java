package org.cyclops.cyclopscore.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.tracking.Versions;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends ClientProxyComponent {

	private final CommonProxy commonProxy = new CommonProxy();

	@Override
	public ModBase getMod() {
		return CyclopsCore._instance;
	}

	@Override
	public void registerPacketHandlers(PacketHandler packetHandler) {
		super.registerPacketHandlers(packetHandler);
        commonProxy.registerPacketHandlers(packetHandler);
	}

    @Override
    public void registerTickHandlers() {
        super.registerTickHandlers();
        commonProxy.registerTickHandlers();
    }

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();
        commonProxy.registerEventHooks();
		FMLCommonHandler.instance().bus().register(new Versions());
	}
    
}
