package org.cyclops.cyclopscore.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.Reference;

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
        // TODO: register a dedicated StreamCodec that has a setter for extraDataWriter.
//        StreamCodec<RegistryFriendlyByteBuf, Object> CODEC = StreamCodec.ofMember(
//                (buff, data) -> {
//                    throw new UnsupportedOperationException("Write is not supported");
//                },
//                buff -> {
//                    extraDataWriter.accept(buff);
//                    return null;
//                }
//        );
//        ExtendedScreenHandlerType<Object> OVEN = new ExtendedScreenHandlerType((syncId, inventory, data) -> ..., CODEC);
//        Registry.register(Registries.MENU, ResourceLocation.parse("todo"), OVEN);


        // TODO: packet buffers are not supported yet. When needed, implement support for ExtendedScreenHandlerFactory which will require registering codecs for containers.
        player.openMenu(containerSupplier);
//        player.openMenu(new ExtendedScreenHandlerFactory<>() {
//            @Nullable
//            @Override
//            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
//                AbstractContainerMenu menu = containerSupplier.createMenu(i, inventory, player);
//                ResourceLocation typeId = BuiltInRegistries.MENU.getKey(menu.getType());
//                StreamCodec<RegistryFriendlyByteBuf, Object> codec = (StreamCodec<RegistryFriendlyByteBuf, Object>) Networking.CODEC_BY_ID.get(typeId);
//                // TODO: call the extraDataWriter setter here.
//                return menu;
//            }
//
//            @Override
//            public Component getDisplayName() {
//                return containerSupplier.getDisplayName();
//            }
//
//            @Override
//            public Object getScreenOpeningData(ServerPlayer player) {
//                return null; // Hack so we can just use our extraDataWriter
//            }
//        });
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
