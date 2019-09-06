package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
			block.onEntityWalk(world, blockPos, entity);
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
	    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, box);
	    return entities;
	}

	/**
	 * Spawns the creature specified by the entity name in the location specified by the last three parameters.
	 * @param world The world.
	 * @param entityName The name of the entity.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @return the entity that was spawned.
	 */
	public static Optional<Entity> spawnEntity(World world, @Nullable ResourceLocation entityName, double x, double y, double z) {
		return EntityType.byKey(entityName.toString()).map((type) -> {
			Entity entity = type.create(world);
			entity.setPosition(x, y, z);
			world.addEntity(entity);
			return entity;
		});
	}
	
	/**
	 * Spawn the entity in the world.
	 * @param world The world.
	 * @param entityLiving The entity to spawn.
	 * @param spawnReason The spawn reason.
	 * @return If the entity was spawned.
	 */
	public static boolean spawnEntity(World world, MobEntity entityLiving, SpawnReason spawnReason) {
		AbstractSpawner spawner = new AbstractSpawner() {
			@Override
			public void broadcastEvent(int id) {

			}

			@Override
			public World getWorld() {
				return world;
			}

			@Override
			public BlockPos getSpawnerPosition() {
				return entityLiving.getPosition();
			}
		};
		Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityLiving, world, (float) entityLiving.posX,
				(float) entityLiving.posY, (float) entityLiving.posZ, spawner, spawnReason);
        if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT)) { //  && entityliving.getCanSpawnHere()
            if (!ForgeEventFactory.doSpecialSpawn(entityLiving, world, (float) entityLiving.posX,
					(float) entityLiving.posY, (float) entityLiving.posZ, spawner, spawnReason)) {
            	world.addEntity(entityLiving);
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
        int x = ((int) Math.ceil(entity.getWidth()));
        int y = ((int) Math.ceil(entity.getHeight()));
        int z = x;
        return new Vec3i(x, y, z);
    }

	/**
	 * Spawn xp orbs at the given player.
	 * @param world The world.
	 * @param player The player.
	 * @param xp The amount of experience to spawn.
	 */
	public static void spawnXpAtPlayer(World world, PlayerEntity player, int xp) {
		if(!world.isRemote()) {
			while (xp > 0) {
				int current;
				current = ExperienceOrbEntity.getXPSplit(xp);
				xp -= current;
				world.addEntity(new ExperienceOrbEntity(world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, current));
			}
		}
	}

	/**
	 * Get the persisted NBT tag from a player.
	 * @param player The player.
	 * @return The player's persisted NBT tag.
	 */
	public static CompoundNBT getPersistedPlayerNbt(PlayerEntity player) {
		CompoundNBT tag = player.getPersistantData();
		CompoundNBT persistedTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		if (persistedTag == null) {
			persistedTag = new CompoundNBT();
			tag.put(PlayerEntity.PERSISTED_NBT_TAG, persistedTag);
		}
		return persistedTag;
	}

}
