package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.item.ItemInformationProviderNeoForge;

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
    public ModBaseNeoForge getMod() {
        return CyclopsCoreNeoForge._instance;
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }

}
