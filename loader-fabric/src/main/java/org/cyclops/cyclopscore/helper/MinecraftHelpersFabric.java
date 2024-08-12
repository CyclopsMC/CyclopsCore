package org.cyclops.cyclopscore.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.Reference;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpersFabric extends MinecraftHelpersCommon {
    @Override
    public boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isMinecraftInitialized() {
        return CyclopsCoreFabric._instance.isLoaded();
    }

    @Override
    public boolean isModdedEnvironment() {
        return FabricLoaderImpl.INSTANCE != null;
    }

    @Override
    public boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isClientSideThread() {
        return isClientSide() && Minecraft.getInstance().level != null
                && Thread.currentThread() == Minecraft.getInstance().level.thread;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return Reference.MOD_VANILLA.equals(modId) || FabricLoader.getInstance().isModLoaded(modId);
    }

}
