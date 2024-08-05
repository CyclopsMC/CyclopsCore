package org.cyclops.cyclopscore.helper;

import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import org.cyclops.cyclopscore.CyclopsCore;

/**
 * @author rubensworks
 */
public class MinecraftHelpersNeoForge extends MinecraftHelpersCommon implements IMinecraftHelpers {
    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isMinecraftInitialized() {
        return CyclopsCore._instance.isLoaded();
    }

    @Override
    public boolean isModdedEnvironment() {
        return MinecraftHelpersNeoForge.class.getClassLoader() instanceof TransformingClassLoader;
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
