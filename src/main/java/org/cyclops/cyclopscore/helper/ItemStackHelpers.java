package org.cyclops.cyclopscore.helper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;
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

        while (itemStack.getCount() > 0) {
            int i = RANDOM.nextInt(21) + 10;

            if (i > itemStack.getCount()) {
                i = itemStack.getCount();
            }

            ItemStack dropStack = itemStack.copy();
            itemStack.shrink(i);
            dropStack.setCount(i);
            EntityItem entityitem = new EntityItem(world, x + (double)offsetX, y + (double)offsetY,
                    z + (double)offsetZ, dropStack);

            float motion = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * (double)motion;
            entityitem.motionY = RANDOM.nextGaussian() * (double)motion + 0.2D;
            entityitem.motionZ = RANDOM.nextGaussian() * (double)motion;
            world.spawnEntity(entityitem);
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
            world.spawnEntity(entityitem);
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
        NonNullList<ItemStack> output = NonNullList.create();
        if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            itemStack.getItem().getSubItems(CreativeTabs.SEARCH, output);
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
        Item item =  Item.REGISTRY.getObject(new ResourceLocation(itemName));
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
     * @deprecated Use {@link ItemStack#areItemStacksEqual} instead.
     */
    @Deprecated // TODO remove in 1.13
    public static boolean areItemStacksIdentical(ItemStack a, ItemStack b) {
        return ItemStack.areItemStacksEqual(a, b);
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
        result = 37 * result + stack.getItemDamage();
        NBTTagCompound tagCompound = stack.getTagCompound();
        result = 37 * result + (tagCompound != null ? tagCompound.hashCode() : 0);
        // Not factoring in capability compatibility. Doing so would require either reflection (slow)
        // or an access transformer, it's highly unlikely that it'd be the only difference between
        // many ItemStacks in practice, and occasional hash code collisions are okay.
        return result;
    }

    /**
     * If the given item can be displayed in the given creative tab.
     * @param item The item.
     * @param creativeTab The creative tab.
     * @return If it can be displayed.
     */
    public static boolean isValidCreativeTab(Item item, @Nullable CreativeTabs creativeTab) {
        for (CreativeTabs itemTab : item.getCreativeTabs()) {
            if (itemTab == creativeTab) {
                return true;
            }
        }
        return creativeTab == null
                || creativeTab == CreativeTabs.SEARCH;
    }

    /**
     * Get all subitems of an item.
     * This is based on {@link Item#getSubItems(CreativeTabs, NonNullList)}.
     * @param itemStack The given item.
     * @return The sub items.
     */
    public static NonNullList<ItemStack> getSubItems(ItemStack itemStack) {
        NonNullList<ItemStack> subItems = NonNullList.create();
        itemStack.getItem().getSubItems(CreativeTabs.SEARCH, subItems);
        return subItems;
    }

    /**
     * If the given stack has a wildcard meta value,
     * return a list of all its subitems,
     * otherwise return a list with as single element itself.
     * @param itemStack The given item.
     * @return The sub items.
     */
    public static NonNullList<ItemStack> getSubItemsIfWildcardMeta(ItemStack itemStack) {
        if (itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
            NonNullList<ItemStack> subItems = NonNullList.create();
            itemStack.getItem().getSubItems(CreativeTabs.SEARCH, subItems);
            return subItems;
        } else {
            return NonNullList.withSize(1, itemStack);
        }
    }

}
