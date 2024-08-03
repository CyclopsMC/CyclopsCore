package org.cyclops.cyclopscore.helper;

import net.minecraft.DetectedVersion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.Arrays;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public class MinecraftHelpersCommon {
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
        int currentTime = (int) world.getGameTime();
        int newTime = currentTime - (currentTime % (MINECRAFT_DAY / 2)) + MINECRAFT_DAY / 2;
        ((ServerLevelData) world.getLevelData()).setGameTime(newTime);
    }

    /**
     * Check if the given player inventory is full.
     * @param player The player.
     * @return If the player does not have a free spot in it's inventory.
     */
    public static boolean isPlayerInventoryFull(Player player) {
        return player.getInventory().getFreeSlot() == -1;
    }

    /**
     * @return The Minecraft version (e.g. "1.14.4")
     */
    public static String getMinecraftVersion() {
        return DetectedVersion.BUILT_IN.getName();
    }

    /**
     * @return The Minecraft major and minor version (e.g. "1.14")
     */
    public static String getMinecraftVersionMajorMinor() {
        return String.join(".", Arrays.asList(getMinecraftVersion().split("\\.")).subList(0, 2));
    }

    /**
     * Comparator for {@link BlockPos}.
     * @param pos1 First pos.
     * @param pos2 Second pos.
     * @return The compared value.
     */
    public static int compareBlockPos(BlockPos pos1, BlockPos pos2) {
        int compX = Integer.compare(pos1.getX(), pos2.getX());
        if(compX == 0) {
            int compY = Integer.compare(pos1.getY(), pos2.getY());
            if(compY == 0) {
                return Integer.compare(pos1.getZ(), pos2.getZ());
            }
            return compY;
        }
        return compX;
    }

    /**
     * Create a new successfull action result.
     * @param result The result element.
     * @param <T> The type.
     * @return The action result.
     */
    public static <T> InteractionResultHolder<T> successAction(T result) {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, result);
    }

}
