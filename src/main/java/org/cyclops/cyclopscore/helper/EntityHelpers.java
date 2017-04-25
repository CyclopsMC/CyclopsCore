package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.List;

/**
 * Helpers for entities.
 * @author rubensworks
 *
 */
public class EntityHelpers {

	/**
	 * The NBT tag name that is used for storing the unique name id for an entity.
	 */
	public static final String NBTTAG_ID = "id";

	/**
	 * This should by called when custom entities collide. It will call the
	 * correct method in {@link Block#onEntityWalk(World, BlockPos, Entity)}.
	 * @param world The world
	 * @param blockPos The position.
	 * @param entity The entity that collides.
	 */
	public static void onEntityCollided(World world, BlockPos blockPos, Entity entity) {
		if (blockPos != null) {
			Block block = world.getBlockState(blockPos).getBlock();
			if (block != null) {
				block.onEntityWalk(world, blockPos, entity);
			}
		}
	}

	/**
	 * Get the list of entities within a certain area.
	 * @param world The world to look in.
	 * @param blockPos The position.
	 * @param area The radius of the area.
	 * @return The list of entities in that area.
	 */
	public static List<Entity> getEntitiesInArea(World world, BlockPos blockPos, int area) {
	    AxisAlignedBB box = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
				blockPos.getX(), blockPos.getY(), blockPos.getZ()).expand(area, area, area);
	    @SuppressWarnings("unchecked")
	    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
	    return entities;
	}
	
	/**
	 * Spawn the entity in the world.
	 * @param world The world.
	 * @param entityLiving The entity to spawn.
	 * @return If the entity was spawned.
	 */
	public static boolean spawnEntity(World world, EntityLiving entityLiving) {
		Result canSpawn = ForgeEventFactory.canEntitySpawn(entityLiving, world, (float) entityLiving.posX,
				(float) entityLiving.posY, (float) entityLiving.posZ);
        if (canSpawn == Result.ALLOW || (canSpawn == Result.DEFAULT)) { //  && entityliving.getCanSpawnHere()
            if (!ForgeEventFactory.doSpecialSpawn(entityLiving, world, (float) entityLiving.posX,
					(float) entityLiving.posY, (float) entityLiving.posZ)) {
            	world.spawnEntityInWorld(entityLiving);
                return true;
            }
        }
        return false;
	}

    /**
     * Get the size of an entity.
     * @param entity The entity.
     * @return The size.
     */
    public static Vec3i getEntitySize(Entity entity) {
        int x = ((int) Math.ceil(entity.width));
        int y = ((int) Math.ceil(entity.height));
        int z = x;
        return new Vec3i(x, y, z);
    }

	/**
	 * Spawn xp orbs at the given player.
	 * @param world The world.
	 * @param player The player.
	 * @param xp The amount of experience to spawn.
	 */
	public static void spawnXpAtPlayer(World world, EntityPlayer player, int xp) {
		if(!world.isRemote) {
			while (xp > 0) {
				int current;
				current = EntityXPOrb.getXPSplit(xp);
				xp -= current;
				world.spawnEntityInWorld(new EntityXPOrb(world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, current));
			}
		}
	}

	/**
	 * Get the persisted NBT tag from a player.
	 * @param player The player.
	 * @return The player's persisted NBT tag.
	 */
	public static NBTTagCompound getPersistedPlayerNbt(EntityPlayer player) {
		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound persistedTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if (persistedTag == null) {
			persistedTag = new NBTTagCompound();
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedTag);
		}
		return persistedTag;
	}

}
