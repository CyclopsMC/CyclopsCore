package org.cyclops.cyclopscore.helper;

import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
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
    public static void setDay(World world, boolean toDay) {
        int currentTime = (int) world.getGameTime();
        int newTime = currentTime - (currentTime % (MINECRAFT_DAY / 2)) + MINECRAFT_DAY / 2;
        world.setGameTime(newTime);
    }

	/**
	 * Check if the given player inventory is full.
	 * @param player The player.
	 * @return If the player does not have a free spot in it's inventory.
	 */
	public static boolean isPlayerInventoryFull(PlayerEntity player) {
	    return player.inventory.getFirstEmptyStack() == -1;
	}

    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public static boolean isModdedEnvironment() {
        return MinecraftHelpers.class.getClassLoader() instanceof TransformingClassLoader;
    }

    /**
     * @return If the user is shifted.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean isShifted() {
        return Screen.hasShiftDown();
    }

    /**
     * Comparator for {@link net.minecraft.util.math.BlockPos}.
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
    public static <T> ActionResult<T> successAction(T result) {
        return new ActionResult<>(ActionResultType.SUCCESS, result);
    }

    /**
     * @return If we are physically running on a client.
     */
    public static boolean isClientSide() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

}
