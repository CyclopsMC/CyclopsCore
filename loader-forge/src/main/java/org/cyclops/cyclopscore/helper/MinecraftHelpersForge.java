package org.cyclops.cyclopscore.helper;

import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.cyclops.cyclopscore.CyclopsCoreForge;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpersForge extends MinecraftHelpersCommon {

    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isMinecraftInitialized() {
        return CyclopsCoreForge._instance.isLoaded();
    }

    @Override
    public boolean isModdedEnvironment() {
        return MinecraftHelpersForge.class.getClassLoader() instanceof TransformingClassLoader;
    }

    @Override
    public boolean isClientSide() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isClientSideThread() {
        return isClientSide() && Minecraft.getInstance().level != null
                && Thread.currentThread() == Minecraft.getInstance().level.thread;
    }

}
