package org.cyclops.cyclopscore.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.cyclops.cyclopscore.CyclopsCoreMainFabric;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpers extends MinecraftHelpersCommon {
    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * @return If minecraft has been fully loaded.
     */
    public static boolean isMinecraftInitialized() {
        return CyclopsCoreMainFabric._instance.isLoaded();
    }

    /**
     * @return If we are physically running on a client.
     */
    public static boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    /**
     * @return If we are physically running on a client and are running in the client thread.
     */
    public static boolean isClientSideThread() {
        return isClientSide() && Minecraft.getInstance().level != null
                && Thread.currentThread() == Minecraft.getInstance().level.thread;
    }

}
