package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TileEntityNBTStorage;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains helper methods for various minecraft specific things.
 * @author immortaleeb
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
     * The types of NBT Tags, used for the second parameter in
     * {@link net.minecraft.nbt.NBTTagCompound#getTagList(String, int)}.
     * @author rubensworks
     *
     */
    @SuppressWarnings("javadoc")
    public enum NBTTag_Types {
		NBTTagEnd, NBTTagByte, NBTTagShort,
		NBTTagInt, NBTTagLong, NBTTagFloat,
		NBTTagDouble, NBTTagByteArray, NBTTagString,
		NBTTagList, NBTTagCompound, NBTTagIntArray
	}

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
     * A list of all the {@link net.minecraftforge.common.ChestGenHooks}.
     * @see net.minecraftforge.common.ChestGenHooks
     */
    public static List<String> CHESTGENCATEGORIES = new LinkedList<String>();
    static {
        CHESTGENCATEGORIES.add(ChestGenHooks.BONUS_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.DUNGEON_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.MINESHAFT_CORRIDOR);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_DESERT_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
        CHESTGENCATEGORIES.add(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_CORRIDOR);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_CROSSING);
        CHESTGENCATEGORIES.add(ChestGenHooks.STRONGHOLD_LIBRARY);
        CHESTGENCATEGORIES.add(ChestGenHooks.VILLAGE_BLACKSMITH);
    }
    
    /**
     * Check if it's day in this world.
     * @param world The world.
     * @return If it is day in the world, checked with the world time.
     */
    public static boolean isDay(World world) {
        return world.getWorldTime() % MINECRAFT_DAY < MINECRAFT_DAY/2;
    }
    
    /**
     * Set the world time to day or night.
     * @param world the world to manipulate time in.
     * @param toDay if true, set to day, otherwise to night.
     */
    public static void setDay(World world, boolean toDay) {
        int currentTime = (int) world.getWorldTime();
        int newTime = currentTime - (currentTime % (MINECRAFT_DAY / 2)) + MINECRAFT_DAY / 2;
        world.setWorldTime(newTime);
    }
    
    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * @param world The world.
     * @param entityID The ID of the entity.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return the entity that was spawned.
     */
    public static Entity spawnCreature(World world, int entityID, double x, double y, double z) {
        return ItemMonsterPlacer.spawnCreature(world, entityID, x, y, z);
    }
    
    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param stack ItemStack to drop
     * @param blockPos The position.
     */
    public static void dropItems(World world, ItemStack stack, BlockPos blockPos) {
        if (stack.stackSize > 0) {
            float offsetMultiply = 0.7F;
            double offsetX = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            double offsetY = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            double offsetZ = (world.rand.nextFloat() * offsetMultiply) + (1.0F - offsetMultiply) * 0.5D;
            EntityItem entityitem = new EntityItem(world, blockPos.getX() + offsetX, blockPos.getY() + offsetY,
                    blockPos.getZ() + offsetZ, stack);
            entityitem.setPickupDelay(10);
    
            world.spawnEntityInWorld(entityitem);
        }
    }
    
    /**
     * Drop an ItemStack into the world
     * @param world the world
     * @param inventory inventory with ItemStacks
     * @param blockPos The position.
     */
    public static void dropItems(World world, IInventory inventory, BlockPos blockPos) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize > 0)
                dropItems(world, inventory.getStackInSlot(i).copy(), blockPos);
        }
    }

	/**
	 * Check if the given player inventory is full.
	 * @param player The player.
	 * @return If the player does not have a free spot in it's inventory.
	 */
	public static boolean isPlayerInventoryFull(EntityPlayer player) {
	    return player.inventory.getFirstEmptyStack() == -1;
	}

    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public static boolean isModdedEnvironment() {
        return MinecraftHelpers.class.getClassLoader() instanceof LaunchClassLoader;
    }

	/**
	 * Check if this code is ran on client side.
	 * @return If we are at client side.
	 */
	public static boolean isClientSide() {
	    return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
	}

    /**
     * Check if two fluid stacks contain the same fluid and have the same amount of that fluid.
     * @param fluid1 A fluid stack.
     * @param fluid2 A fluid stack.
     * @return If they are equal.
     */
    public static boolean isFluidAndAmountEqual(FluidStack fluid1, FluidStack fluid2) {
        if((fluid1 == null && fluid2 != null) || (fluid1 != null && fluid2 == null)) {
            return false;
        }
        if(fluid1 == null) { // fluid2 is always null now
            return true;
        }
        return fluid1.getFluidID() == fluid2.getFluidID() && fluid1.amount == fluid2.amount;
    }

    /**
     * @return If the user is shifted.
     */
    @SideOnly(Side.CLIENT)
    public static boolean isShifted() {
        return Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    /**
     * This method should be called when a BlockContainer is destroyed
     * @param block The blockState.
     * @param world world
     * @param blockPos The position.
     * @param saveNBT If the NBT data should be saved to the dropped item.
     */
    public static void preDestroyBlock(ConfigurableBlockContainer block, World world, BlockPos blockPos, boolean saveNBT) {
        TileEntity tile = world.getTileEntity(blockPos);

        if (tile instanceof IInventory && !world.isRemote) {
            dropItems(world, (IInventory) tile, blockPos);
            InventoryHelpers.clearInventory((IInventory) tile);
        }

        if (tile instanceof CyclopsTileEntity && saveNBT) {
            // Cache
            CyclopsTileEntity ecTile = ((CyclopsTileEntity) tile);
            TileEntityNBTStorage.TAG = ecTile.getNBTTagCompound();
            block.writeAdditionalInfo(tile, TileEntityNBTStorage.TAG);

            ecTile.destroy();
        } else {
            TileEntityNBTStorage.TAG = null;
        }
    }

    /**
     * This method should be called after a BlockContainer is destroyed
     * @param world world
     * @param blockPos The position.
     */
    public static void postDestroyBlock(IBlockAccess world, BlockPos blockPos) {
        // Does nothing for now.
    }

    /**
     * Comparator for {@link net.minecraft.util.BlockPos}.
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
        }
        return compX;
    }

}
