package org.cyclops.cyclopscore.proxy;

import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.item.ItemInformationProviderForge;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyForge extends ClientProxyComponentForge {

    public ClientProxyForge() {
        super(new CommonProxyForge());

        MinecraftForge.EVENT_BUS.addListener(ItemInformationProviderForge::onTooltip);
    }

    @Override
    public ModBaseForge<?> getMod() {
        return CyclopsCoreForge._instance;
    }

}
