package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.helper.IModHelpers;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorldForgeRegistrar {

    @SubscribeEvent
    public static void onMainMenuInit(ScreenEvent.Init.Pre event) {
        // Add a button to the main menu and disable music if we're in a dev environment
        if (IModHelpers.get().getMinecraftHelpers().isDevEnvironment() && GeneralConfig.devWorldButton) {
            GuiMainMenuExtensionDevWorld.onMainMenuInit(event.getScreen().getMinecraft(), event.getScreen());
        }

        // And disable music
        if (IModHelpers.get().getMinecraftHelpers().isDevEnvironment() && GeneralConfig.devDisableMusic) {
            if (event.getScreen() instanceof TitleScreen) {
                Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MUSIC).set(0.0);
            }
        }
    }

}
