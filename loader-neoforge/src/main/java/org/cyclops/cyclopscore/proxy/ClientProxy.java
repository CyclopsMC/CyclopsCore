package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemInformationProviderNeoForge;
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

        NeoForge.EVENT_BUS.addListener(ItemInformationProviderNeoForge::onTooltip);
    }

    @Override
    public ModBase getMod() {
        return CyclopsCore._instance;
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        NeoForge.EVENT_BUS.register(new Versions());
    }

}
