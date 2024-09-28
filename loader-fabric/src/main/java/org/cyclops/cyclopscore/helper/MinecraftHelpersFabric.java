package org.cyclops.cyclopscore.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.impl.screenhandler.Networking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.config.StreamCodecConsumerHack;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpersFabric extends MinecraftHelpersCommon {

    private WeakReference<MinecraftServer> server;

    public MinecraftHelpersFabric() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
           this.server = new WeakReference<>(server);
        });
    }

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
        player.openMenu(new ExtendedScreenHandlerFactory<Boolean>() {
            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                AbstractContainerMenu menu = containerSupplier.createMenu(i, inventory, player);
                ResourceLocation typeId = BuiltInRegistries.MENU.getKey(menu.getType());

                // Hack to pass our extraDataWriter to the gui without requiring a custom codec
                StreamCodec<RegistryFriendlyByteBuf, ?> codec = (StreamCodec<RegistryFriendlyByteBuf, Object>) Networking.CODEC_BY_ID.get(typeId);
                if (codec instanceof StreamCodecConsumerHack streamCodecConsumerHack) {
                    streamCodecConsumerHack.setExtraDataWriter(extraDataWriter);
                }

                return menu;
            }

            @Override
            public Component getDisplayName() {
                return containerSupplier.getDisplayName();
            }

            @Override
            public Boolean getScreenOpeningData(ServerPlayer player) {
                return true; // Hack so we can just use our extraDataWriter
            }

            @Override
            public boolean shouldCloseCurrentScreen() {
                return containerSupplier.shouldCloseCurrentScreen();
            }
        });
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return this.server.get();
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }
}
