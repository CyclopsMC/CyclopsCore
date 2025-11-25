package org.cyclops.cyclopscore.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.classloading.transformation.TransformingClassLoader;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class MinecraftHelpersNeoForge extends MinecraftHelpersCommon implements IMinecraftHelpers {

    @Override
    public boolean isDevEnvironment() {
        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public boolean isMinecraftInitialized() {
        return CyclopsCoreNeoForge._instance.isLoaded();
    }

    @Override
    public boolean isModdedEnvironment() {
        return MinecraftHelpersNeoForge.class.getClassLoader() instanceof TransformingClassLoader;
    }

    @Override
    public boolean isClientSide() {
        return FMLEnvironment.getDist() == Dist.CLIENT;
    }

    @Override
    public boolean isClientSideThread() {
        return isClientSide() && Minecraft.getInstance().level != null
                && Thread.currentThread() == Minecraft.getInstance().level.thread;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return Reference.MOD_VANILLA.equals(modId) || ModList.get().isLoaded(modId);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
        player.openMenu(containerSupplier, extraDataWriter::accept);
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public void sendRecipesToClients(Supplier<Collection<RecipeType<?>>> recipeTypes) {
        NeoForge.EVENT_BUS.addListener(false, OnDatapackSyncEvent.class, e -> e.sendRecipes(recipeTypes.get()));
    }
}
