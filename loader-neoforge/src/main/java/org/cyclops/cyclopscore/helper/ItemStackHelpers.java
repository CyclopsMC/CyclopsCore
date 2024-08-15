package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import java.util.Random;

/**
 * Contains helper methods for various itemstack specific things.
 * @author rubensworks
 */
@Deprecated // TODO: remove in next major version (after porting hasPlayerItem with PlayerExtendedInventoryIterator)
public final class ItemStackHelpers {

    private static final Random RANDOM = new Random();

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param pos The position
     * @param itemStack the item stack
     */
    public static void spawnItemStack(Level world, BlockPos pos, ItemStack itemStack) {
        IModHelpers.get().getItemStackHelpers().spawnItemStack(world, pos, itemStack);
    }

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack the item stack
     */
    public static void spawnItemStack(Level world, double x, double y, double z, ItemStack itemStack) {
        IModHelpers.get().getItemStackHelpers().spawnItemStack(world, x, y, z, itemStack);
    }

    /**
     * Spawn an entity in the direction of a player without setting a pickup delay.
     * @param world The world
     * @param pos The position to spawn an
     * @param stack The stack to spawn
     * @param player The player to direct the motion to
     */
    public static void spawnItemStackToPlayer(Level world, BlockPos pos, ItemStack stack, Player player) {
        IModHelpers.get().getItemStackHelpers().spawnItemStackToPlayer(world, pos, stack, player);
    }



    /**
     * Check if the given player has at least one of the given item.
     * @param player The player.
     * @param item The item to search in the inventory.
     * @return If the player has the item.
     */
    public static boolean hasPlayerItem(Player player, Item item) { // TODO: to port to helpers
        for(PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
            ItemStack itemStack = it.next();
            if (itemStack != null && itemStack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse a string to an itemstack.
     * Expects the format "domain:itemname:amount"
     * The domain and itemname are mandatory, the rest is optional.
     * @param itemStackString The string to parse.
     * @return The itemstack.
     * @throws IllegalArgumentException If the string was incorrectly formatted.
     */
    public static ItemStack parseItemStack(String itemStackString) {
        return IModHelpers.get().getItemStackHelpers().parseItemStack(itemStackString);
    }

    /**
     * Get a hash code would satisfy the requirements of {@link Object#hashCode}
     * if {@link ItemStack#isSameItem(ItemStack, ItemStack)} stood in for {@link Object#equals}.
     * @param stack The itemstack.
     * @return The hash code.
     */
    public static int getItemStackHashCode(ItemStack stack) {
        return IModHelpers.get().getItemStackHelpers().getItemStackHashCode(stack);
    }

}
