package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Contains helper methods for various itemstack specific things.
 * @author rubensworks
 */
public final class ItemStackHelpers {

    private static final Random RANDOM = new Random();

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param pos The position
     * @param itemStack the item stack
     */
    public static void spawnItemStack(World world, BlockPos pos, ItemStack itemStack) {
        spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
    }

    /**
     * Spawn an itemstack into the world.
     * @param world The world
     * @param x X
     * @param y Y
     * @param z Z
     * @param itemStack the item stack
     */
    public static void spawnItemStack(World world, double x, double y, double z, ItemStack itemStack) {
        float offsetX = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetY = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetZ = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (itemStack.getCount() > 0) {
            int i = RANDOM.nextInt(21) + 10;

            if (i > itemStack.getCount()) {
                i = itemStack.getCount();
            }

            ItemStack dropStack = itemStack.copy();
            itemStack.shrink(i);
            dropStack.setCount(i);
            ItemEntity entityitem = new ItemEntity(world, x + (double)offsetX, y + (double)offsetY,
                    z + (double)offsetZ, dropStack);

            float motion = 0.05F;
            entityitem.setMotion(
                    RANDOM.nextGaussian() * (double)motion,
                    RANDOM.nextGaussian() * (double)motion + 0.2D,
                    RANDOM.nextGaussian() * (double)motion);
            world.addEntity(entityitem);
        }
    }

    /**
     * Spawn an entity in the direction of a player without setting a pickup delay.
     * @param world The world
     * @param pos The position to spawn an
     * @param stack The stack to spawn
     * @param player The player to direct the motion to
     */
    public static void spawnItemStackToPlayer(World world, BlockPos pos, ItemStack stack, PlayerEntity player) {
        if (!world.isRemote()) {
            float f = 0.5F;

            double xo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double yo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double zo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            ItemEntity entityitem = new ItemEntity(world, (double)pos.getX() + xo, (double)pos.getY() + yo, (double)pos.getZ() + zo, stack);

            double d0 = 8.0D;
            double d1 = (player.getPosX() - entityitem.getPosX()) / d0;
            double d2 = (player.getPosY() + (double)player.getEyeHeight() - entityitem.getPosY()) / d0;
            double d3 = (player.getPosZ() - entityitem.getPosZ()) / d0;

            entityitem.setMotion(entityitem.getMotion().add(d1, d2, d3));
            entityitem.setNoPickupDelay();
            world.addEntity(entityitem);
        }
    }



    /**
     * Check if the given player has at least one of the given item.
     * @param player The player.
     * @param item The item to search in the inventory.
     * @return If the player has the item.
     */
    public static boolean hasPlayerItem(PlayerEntity player, Item item) {
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
        String[] split = itemStackString.split(":");
        String itemName = split[0] + ":" + split[1];
        Item item =  ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if(item == null) {
            throw new IllegalArgumentException("Invalid ItemStack item: " + itemName);
        }
        int amount = 1;
        if(split.length > 2) {
            try {
                amount = Integer.parseInt(split[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ItemStack amount: " + split[2]);
            }
        }
        return new ItemStack(item, amount);
    }

    /**
     * Get a hash code would satisfy the requirements of {@link Object#hashCode}
     * if {@link ItemStack#areItemStacksEqual} stood in for {@link Object#equals}.
     * @param stack The itemstack.
     * @return The hash code.
     */
    public static int getItemStackHashCode(ItemStack stack) {
        if(stack.isEmpty()) return ItemStack.EMPTY.hashCode();
        int result = 1;
        result = 37 * result + stack.getCount();
        result = 37 * result + stack.getItem().hashCode();
        CompoundNBT tagCompound = stack.getTag();
        result = 37 * result + (tagCompound != null ? tagCompound.hashCode() : 0);
        // Not factoring in capability compatibility. Doing so would require either reflection (slow)
        // or an access transformer, it's highly unlikely that it'd be the only difference between
        // many ItemStacks in practice, and occasional hash code collisions are okay.
        return result;
    }

    /**
     * If the given item can be displayed in the given creative tab.
     * @param item The item.
     * @param itemGroup The creative tab.
     * @return If it can be displayed.
     */
    public static boolean isValidCreativeTab(Item item, @Nullable ItemGroup itemGroup) {
        for (ItemGroup itemTab : item.getCreativeTabs()) {
            if (itemTab == itemGroup) {
                return true;
            }
        }
        return itemGroup == null
                || itemGroup == ItemGroup.SEARCH;
    }

}
