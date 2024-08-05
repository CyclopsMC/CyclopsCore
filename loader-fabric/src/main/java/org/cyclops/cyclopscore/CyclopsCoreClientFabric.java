package org.cyclops.cyclopscore;

import net.fabricmc.api.ClientModInitializer;
import org.cyclops.cyclopscore.client.gui.GuiMainMenuExtensionDevWorldFabricRegistrar;
import org.cyclops.cyclopscore.item.ItemInformationProviderFabric;

/**
 * @author rubensworks
 */
public class CyclopsCoreClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GuiMainMenuExtensionDevWorldFabricRegistrar.load();
        ItemInformationProviderFabric.load();
    }
}
