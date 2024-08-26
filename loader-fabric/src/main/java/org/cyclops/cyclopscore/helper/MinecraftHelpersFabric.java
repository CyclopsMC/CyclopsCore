package org.cyclops.cyclopscore.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.Reference;

import java.util.function.Consumer;

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

    @Override
    public void openMenu(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
        throw new UnsupportedOperationException("openMenu has not been implemented yet for Fabric");
//        player.openMenu(new ExtendedScreenHandlerFactory<>() {
//            @Nullable
//            @Override
//            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
//                return containerSupplier.createMenu(i, inventory, player);
//            }
//
//            @Override
//            public Component getDisplayName() {
//                return containerSupplier.getDisplayName();
//            }
//
//            @Override
//            public Object getScreenOpeningData(ServerPlayer player) {
//                return null;
//            }
//        });
    }

}
