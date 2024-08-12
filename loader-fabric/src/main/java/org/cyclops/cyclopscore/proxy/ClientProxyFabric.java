package org.cyclops.cyclopscore.proxy;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.client.gui.GuiMainMenuExtensionDevWorldFabricRegistrar;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.item.ItemInformationProviderFabric;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyFabric extends ClientProxyComponentFabric {

    public ClientProxyFabric() {
        super(new CommonProxyFabric());
    }

    @Override
    public ModBaseFabric<?> getMod() {
        return CyclopsCoreFabric._instance;
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();

        ItemTooltipCallback.EVENT.register(ItemInformationProviderFabric::onTooltip);
        ScreenEvents.AFTER_INIT.register(GuiMainMenuExtensionDevWorldFabricRegistrar::afterInit);
    }
}
