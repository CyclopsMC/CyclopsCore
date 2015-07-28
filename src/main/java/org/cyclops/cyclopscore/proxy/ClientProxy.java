package org.cyclops.cyclopscore.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Versions;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends ClientProxyComponent {

	public ClientProxy() {
		super(new CommonProxy());
	}

	@Override
	public ModBase getMod() {
		return CyclopsCore._instance;
	}

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();
		FMLCommonHandler.instance().bus().register(new Versions());
	}
    
}
