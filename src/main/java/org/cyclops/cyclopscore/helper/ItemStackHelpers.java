package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import java.util.List;
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

            entityitem.motionX += d1;
            entityitem.motionY += d2;
            entityitem.motionZ += d3;

            entityitem.setNoPickupDelay();
            world.spawnEntityInWorld(entityitem);
        }
    }



    /**
     * Check if the given player has at least one of the given item.
     * @param player The player.
     * @param item The item to search in the inventory.
     * @return If the player has the item.
     */
    public static boolean hasPlayerItem(EntityPlayer player, Item item) {
        for(PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
            ItemStack itemStack = it.next();
            if (itemStack != null && itemStack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of variants from the given stack if its damage value is the wildcard value,
     * otherwise the list will only contain the given itemstack.
     * @param itemStack The itemstack
     * @return The list of variants.
     */
    public static List<ItemStack> getVariants(ItemStack itemStack) {
        List<ItemStack> output = Lists.newLinkedList();
        if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            itemStack.getItem().getSubItems(itemStack.getItem(), null, output);
        } else {
            output.add(itemStack);
        }
        return output;
    }

    /**
     * Parse a string to an itemstack.
     * Expects the format "domain:itemname:amount:meta"
     * The domain and itemname are mandatory, the rest is optional.
     * @param itemStackString The string to parse.
     * @return The itemstack.
     * @throws IllegalArgumentException If the string was incorrectly formatted.
     */
    public static ItemStack parseItemStack(String itemStackString) {
        String[] split = itemStackString.split(":");
        String itemName = split[0] + ":" + split[1];
        Item item =  Item.itemRegistry.getObject(new ResourceLocation(itemName));
        if(item == null) {
            throw new IllegalArgumentException("Invalid ItemStack item: " + itemName);
        }
        int amount = 1;
        int meta = 0;
        if(split.length > 2) {
            try {
                amount = Integer.parseInt(split[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ItemStack amount: " + split[2]);
            }
            if(split.length > 3) {
                try {
                    meta = Integer.parseInt(split[3]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid ItemStack meta: " + split[3]);
                }
            }
        }
        return new ItemStack(item, amount, meta);
    }

    /**
     * If the given itemstacks are completely identical, including their stack size.
     * @param a The first itemstack.
     * @param b The second itemstack.
     * @return If they are completely equal.
     */
    public static boolean areItemStacksIdentical(ItemStack a, ItemStack b) {
        return ItemStack.areItemStacksEqual(a, b) && ((a == null && b == null) || (a != null && a.stackSize == b.stackSize));
    }

}
