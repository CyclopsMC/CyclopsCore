package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.cyclops.cyclopscore.GeneralConfig;

@EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorldNeoForgeRegistrar {

    @SubscribeEvent
    public static void onMainMenuInit(ScreenEvent.Init.Pre event) {
        // Add a button to the main menu if we're in a dev environment
        if (GeneralConfig.devWorldButton) {
            GuiMainMenuExtensionDevWorld.onMainMenuInit(event.getScreen().getMinecraft(), event.getScreen());
        }

        // And disable music
        if (GeneralConfig.devDisableMusic) {
            if (event.getScreen() instanceof TitleScreen) {
                Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0);
            }
        }
    }

}
