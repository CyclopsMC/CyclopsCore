package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
@Deprecated // TODO: remove in next major version
public class MinecraftHelpers {
    /**
     * The length of one Minecraft day.
     */
    public static final int MINECRAFT_DAY = 24000;
    /**
     * The amount of steps there are in a vanilla comparator.
     */
    public static final int COMPARATOR_MULTIPLIER = 15;
    /**
     * The amount of ticks that go in one second.
     */
    public static final int SECOND_IN_TICKS = 20;

    /**
     * Cause a regular blockState update.
     */
    public static final int BLOCK_NOTIFY = 1;
    /**
     * Send a blockState update to the client.
     */
    public static final int BLOCK_NOTIFY_CLIENT = 2;
    /**
     * Stop the blockState from re-rendering.
     */
    public static final int BLOCK_NOTIFY_NO_RERENDER = 4;

    /**
     * Set the world time to day or night.
     * @param world the world to manipulate time in.
     * @param toDay if true, set to day, otherwise to night.
     */
    public static void setDay(ServerLevel world, boolean toDay) {
        IModHelpers.get().getMinecraftHelpers().setDay(world, toDay);
    }

    /**
     * Check if the given player inventory is full.
     * @param player The player.
     * @return If the player does not have a free spot in it's inventory.
     */
    public static boolean isPlayerInventoryFull(Player player) {
        return IModHelpers.get().getMinecraftHelpers().isPlayerInventoryFull(player);
    }

    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public static boolean isDevEnvironment() {
        return IModHelpers.get().getMinecraftHelpers().isDevEnvironment();
    }

    /**
     * @return If minecraft has been fully loaded.
     */
    public static boolean isMinecraftInitialized() {
        return IModHelpers.get().getMinecraftHelpers().isMinecraftInitialized();
    }

    /**
     * @return The Minecraft version (e.g. "1.14.4")
     */
    public static String getMinecraftVersion() {
        return IModHelpers.get().getMinecraftHelpers().getMinecraftVersion();
    }

    /**
     * @return The Minecraft major and minor version (e.g. "1.14")
     */
    public static String getMinecraftVersionMajorMinor() {
        return IModHelpers.get().getMinecraftHelpers().getMinecraftVersionMajorMinor();
    }

    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public static boolean isModdedEnvironment() {
        return IModHelpers.get().getMinecraftHelpers().isModdedEnvironment();
    }

    /**
     * @return If the user is shifted.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isShifted() {
        return Screen.hasShiftDown();
    }

    /**
     * Comparator for {@link net.minecraft.core.BlockPos}.
     * @param pos1 First pos.
     * @param pos2 Second pos.
     * @return The compared value.
     */
    public static int compareBlockPos(BlockPos pos1, BlockPos pos2) {
        return IModHelpers.get().getMinecraftHelpers().compareBlockPos(pos1, pos2);
    }

    /**
     * Create a new successfull action result.
     * @param result The result element.
     * @param <T> The type.
     * @return The action result.
     */
    public static <T> InteractionResultHolder<T> successAction(T result) {
        return IModHelpers.get().getMinecraftHelpers().successAction(result);
    }

    /**
     * @return If we are physically running on a client.
     */
    public static boolean isClientSide() {
        return IModHelpers.get().getMinecraftHelpers().isClientSide();
    }

    /**
     * @return If we are physically running on a client and are running in the client thread.
     */
    public static boolean isClientSideThread() {
        return IModHelpers.get().getMinecraftHelpers().isClientSideThread();
    }

}
