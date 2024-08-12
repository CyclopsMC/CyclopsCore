package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.sounds.SoundSource;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * @author rubensworks
 */
public class GuiMainMenuExtensionDevWorldFabricRegistrar {
    public static void afterInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight) {
        // Add a button to the main menu and disable music if we're in a dev environment
        if (IModHelpers.get().getMinecraftHelpers().isDevEnvironment() && GeneralConfig.devWorldButton) {
            GuiMainMenuExtensionDevWorld.onMainMenuInit(minecraft, screen);
        }

        // And disable music
        if (IModHelpers.get().getMinecraftHelpers().isDevEnvironment() && GeneralConfig.devDisableMusic) {
            if (screen instanceof TitleScreen) {
                Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0);
            }
        }
    }
}
