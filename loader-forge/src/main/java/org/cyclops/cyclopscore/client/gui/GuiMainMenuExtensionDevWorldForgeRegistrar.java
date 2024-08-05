package org.cyclops.cyclopscore.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.CyclopsCoreForge;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GuiMainMenuExtensionDevWorldForgeRegistrar {

    @SubscribeEvent
    public static void onMainMenuInit(ScreenEvent.Init.Pre event) {
        // Add a button to the main menu if we're in a dev environment
        if (CyclopsCoreForge._instance.getModHelpers().getMinecraftHelpers().isDevEnvironment()) {
            GuiMainMenuExtensionDevWorld.onMainMenuInit(event.getScreen().getMinecraft(), event.getScreen());
        }
    }

}
