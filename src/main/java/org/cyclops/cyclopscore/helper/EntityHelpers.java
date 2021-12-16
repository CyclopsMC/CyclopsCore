package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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
	 * correct method in {@link Block#stepOn(Level, BlockPos, BlockState, Entity)}.
	 * @param world The world
	 * @param blockPos The position.
	 * @param entity The entity that collides.
	 */
	public static void onEntityCollided(Level world, BlockPos blockPos, BlockState blockState, Entity entity) {
		if (blockPos != null) {
			Block block = world.getBlockState(blockPos).getBlock();
			block.stepOn(world, blockPos, blockState, entity);
		}
	}

	/**
	 * Get the list of entities within a certain area.
	 * @param world The world to look in.
	 * @param blockPos The position.
	 * @param area The radius of the area.
	 * @return The list of entities in that area.
	 */
	public static List<Entity> getEntitiesInArea(Level world, BlockPos blockPos, int area) {
	    AABB box = new AABB(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
				blockPos.getX(), blockPos.getY(), blockPos.getZ()).inflate(area, area, area);
	    List<Entity> entities = world.getEntitiesOfClass(Entity.class, box);
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
	public static Optional<Entity> spawnEntity(Level world, @Nullable ResourceLocation entityName, double x, double y, double z) {
		return EntityType.byString(entityName.toString()).map((type) -> {
			Entity entity = type.create(world);
			entity.setPos(x, y, z);
			world.addFreshEntity(entity);
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
	public static boolean spawnEntity(Level world, Mob entityLiving, MobSpawnType spawnReason) {
		BaseSpawner spawner = new BaseSpawner() {
			@Override
			public void broadcastEvent(Level level, BlockPos blockPos, int id) {

			}
		};
		Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(entityLiving, world, (float) entityLiving.getX(),
				(float) entityLiving.getY(), (float) entityLiving.getZ(), spawner, spawnReason);
        if (canSpawn == Event.Result.ALLOW || (canSpawn == Event.Result.DEFAULT)) { //  && entityliving.getCanSpawnHere()
            if (!ForgeEventFactory.doSpecialSpawn(entityLiving, world, (float) entityLiving.getX(),
					(float) entityLiving.getY(), (float) entityLiving.getZ(), spawner, spawnReason)) {
            	world.addFreshEntity(entityLiving);
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
        int x = ((int) Math.ceil(entity.getBbWidth()));
        int y = ((int) Math.ceil(entity.getBbHeight()));
        int z = x;
        return new Vec3i(x, y, z);
    }

	/**
	 * Spawn xp orbs at the given player.
	 * @param world The world.
	 * @param player The player.
	 * @param xp The amount of experience to spawn.
	 */
	public static void spawnXpAtPlayer(Level world, Player player, int xp) {
		if(!world.isClientSide()) {
			while (xp > 0) {
				int current;
				current = ExperienceOrb.getExperienceValue(xp);
				xp -= current;
				world.addFreshEntity(new ExperienceOrb(world, player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, current));
			}
		}
	}

	/**
	 * Get the persisted NBT tag from a player.
	 * @param player The player.
	 * @return The player's persisted NBT tag.
	 */
	public static CompoundTag getPersistedPlayerNbt(Player player) {
		CompoundTag tag = player.getPersistentData();
		CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG);
		if (persistedTag == null) {
			persistedTag = new CompoundTag();
			tag.put(Player.PERSISTED_NBT_TAG, persistedTag);
		}
		return persistedTag;
	}

}
