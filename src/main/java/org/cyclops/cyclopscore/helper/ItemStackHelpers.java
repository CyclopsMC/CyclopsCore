package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Contains helper methods for various itemstack specific things.
 * @author rubensworks
 */
public final class ItemStackHelpers {

    private static final Random RANDOM = new Random();

    /**
     * Get the tag compound from an item safely.
     * If it does not exist yet, it will create and save a new tag compound.
     * @param itemStack The item to get the tag compound from.
     * @return The tag compound.
     */
    public static NBTTagCompound getSafeTagCompound(ItemStack itemStack) {
        if(!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }

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

        while (itemStack.stackSize > 0) {
            int i = RANDOM.nextInt(21) + 10;

            if (i > itemStack.stackSize) {
                i = itemStack.stackSize;
            }

            itemStack.stackSize -= i;
            EntityItem entityitem = new EntityItem(world, x + (double)offsetX, y + (double)offsetY,
                    z + (double)offsetZ, new ItemStack(itemStack.getItem(), i, itemStack.getMetadata()));

            if (itemStack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            float motion = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * (double)motion;
            entityitem.motionY = RANDOM.nextGaussian() * (double)motion + 0.2D;
            entityitem.motionZ = RANDOM.nextGaussian() * (double)motion;
            world.spawnEntityInWorld(entityitem);
        }
    }

    /**
     * Spawn an entity in the direction of a player without setting a pickup delay.
     * @param world The world
     * @param pos The position to spawn an
     * @param stack The stack to spawn
     * @param player The player to direct the motion to
     */
    public static void spawnItemStackToPlayer(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        if (!world.isRemote) {
            float f = 0.5F;

            double xo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double yo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double zo = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)pos.getX() + xo, (double)pos.getY() + yo, (double)pos.getZ() + zo, stack);

            double d0 = 8.0D;
            double d1 = (player.posX - entityitem.posX) / d0;
            double d2 = (player.posY + (double)player.getEyeHeight() - entityitem.posY) / d0;
            double d3 = (player.posZ - entityitem.posZ) / d0;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D) {
                d5 *= d5;
                entityitem.motionX += d1 / d4 * d5 * 0.1D;
                entityitem.motionY += d2 / d4 * d5 * 0.1D;
                entityitem.motionZ += d3 / d4 * d5 * 0.1D;
            }

            entityitem.setNoPickupDelay();
            world.spawnEntityInWorld(entityitem);
        }
    }

}
