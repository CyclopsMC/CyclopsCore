package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public interface IItemStackHelpers {

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param pos The position
     * @param itemStack the item stack
     */
    public void spawnItemStack(Level world, BlockPos pos, ItemStack itemStack);

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack the item stack
     */
    public void spawnItemStack(Level world, double x, double y, double z, ItemStack itemStack);

    /**
     * Spawn an entity in the direction of a player without setting a pickup delay.
     * @param world The world
     * @param pos The position to spawn an
     * @param stack The stack to spawn
     * @param player The player to direct the motion to
     */
    public void spawnItemStackToPlayer(Level world, BlockPos pos, ItemStack stack, Player player);

    /**
     * Parse a string to an itemstack.
     * Expects the format "domain:itemname:amount"
     * The domain and itemname are mandatory, the rest is optional.
     * @param itemStackString The string to parse.
     * @return The itemstack.
     * @throws IllegalArgumentException If the string was incorrectly formatted.
     */
    public ItemStack parseItemStack(String itemStackString);

    /**
     * Get a hash code would satisfy the requirements of {@link Object#hashCode}
     * if {@link ItemStack#isSameItem(ItemStack, ItemStack)} stood in for {@link Object#equals}.
     * @param stack The itemstack.
     * @return The hash code.
     */
    public int getItemStackHashCode(ItemStack stack);

    /**
     * @param itemStack An item stack.
     * @return True if this item has a crafting remaining item
     */
    public boolean hasCraftingRemainingItem(ItemStack itemStack);

    /**
     * @param itemStack An item stack.
     * @return The resulting item stack.
     */
    public ItemStack getCraftingRemainingItem(ItemStack itemStack);

}
