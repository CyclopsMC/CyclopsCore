package org.cyclops.cyclopscore.client.gui;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.cyclops.cyclopscore.CyclopsCoreMainFabric;

/**
 * @author rubensworks
 */
public class GuiMainMenuExtensionDevWorldFabricRegistrar {

    public static void load() {
        ScreenEvents.AFTER_INIT.register(GuiMainMenuExtensionDevWorldFabricRegistrar::afterInit);
    }

    private static void afterInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight) {
        // Add a button to the main menu if we're in a dev environment
        if (CyclopsCoreMainFabric._instance.getModHelpers().getMinecraftHelpers().isDevEnvironment()) {
            GuiMainMenuExtensionDevWorld.onMainMenuInit(minecraft, screen);
        }
    }
}
