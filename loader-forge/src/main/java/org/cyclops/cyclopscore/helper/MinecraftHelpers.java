package org.cyclops.cyclopscore.helper;

import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.cyclops.cyclopscore.CyclopsCoreForge;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpers extends MinecraftHelpersCommon {
    /**
     * @return If the user is shifted.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isShifted() {
        return Screen.hasShiftDown();
    }

    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public static boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    /**
     * @return If minecraft has been fully loaded.
     */
    public static boolean isMinecraftInitialized() {
        return CyclopsCoreForge._instance.isLoaded();
    }
    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public static boolean isModdedEnvironment() {
        return MinecraftHelpers.class.getClassLoader() instanceof TransformingClassLoader;
    }

    /**
     * @return If we are physically running on a client.
     */
    public static boolean isClientSide() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    /**
     * @return If we are physically running on a client and are running in the client thread.
     */
    public static boolean isClientSideThread() {
        return isClientSide() && Minecraft.getInstance().level != null
                && Thread.currentThread() == Minecraft.getInstance().level.thread;
    }

}
