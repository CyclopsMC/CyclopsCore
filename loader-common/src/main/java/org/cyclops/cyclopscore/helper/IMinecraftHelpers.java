package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public interface IMinecraftHelpers {

    /**
     * @return The length of one Minecraft day.
     */
    public int getDayLength();

    /**
     * @return The amount of steps there are in a vanilla comparator.
     */
    public int getComparatorMultiplier();

    /**
     * @return The amount of ticks that go in one second.
     */
    public int getSecondInTicks();

    /**
     * @return Cause a regular blockState update.
     */
    public int getBlockNotify();

    /**
     * @return Send a blockState update to the client.
     */
    public int getBlockNotifyClient();

    /**
     * @return Stop the blockState from re-rendering.
     */
    public int getBlockNotifyNoRerender();

    /**
     * Set the world time to day or night.
     * @param world the world to manipulate time in.
     * @param toDay if true, set to day, otherwise to night.
     */
    public void setDay(ServerLevel world, boolean toDay);

    /**
     * Check if the given player inventory is full.
     * @param player The player.
     * @return If the player does not have a free spot in it's inventory.
     */
    public boolean isPlayerInventoryFull(Player player);

    /**
     * @return The Minecraft version (e.g. "1.14.4")
     */
    public String getMinecraftVersion();

    /**
     * @return The Minecraft major and minor version (e.g. "1.14")
     */
    public String getMinecraftVersionMajorMinor();

    /**
     * Comparator for {@link BlockPos}.
     * @param pos1 First pos.
     * @param pos2 Second pos.
     * @return The compared value.
     */
    public int compareBlockPos(BlockPos pos1, BlockPos pos2);

    /**
     * Create a new successfull action result.
     * @param result The result element.
     * @param <T> The type.
     * @return The action result.
     */
    public <T> InteractionResultHolder<T> successAction(T result);

    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public boolean isDevEnvironment();

    /**
     * @return If minecraft has been fully loaded.
     */
    public boolean isMinecraftInitialized();

    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public boolean isModdedEnvironment();

    /**
     * @return If we are physically running on a client.
     */
    public boolean isClientSide();

    /**
     * @return If we are physically running on a client and are running in the client thread.
     */
    public boolean isClientSideThread();

    /**
     * Check if the given mod is available and loaded.
     * @param modId A mod's id.
     * @return If it is loaded.
     */
    public boolean isModLoaded(String modId);

    /**
     * Open a menu with the given extra data.
     * @param containerSupplier A supplier of container properties including the registry name of the container
     * @param extraDataWriter Consumer to write any additional data the GUI needs
     */
    public void openMenu(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter);

}
