package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

/**
 * A dynamic model that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingDynamicItemAndBlockModel extends DynamicItemAndBlockModel {

    protected final BlockState blockState;
    protected final Direction facing;
    protected final long rand;

    protected final ItemStack itemStack;
    protected final World world;
    protected final LivingEntity entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = 0;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(BlockState blockState, Direction facing, long rand) {
        super(false, false);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, World world, LivingEntity entity) {
        super(false, true);
        this.blockState = null;
        this.facing = null;
        this.rand = 0L;

        this.itemStack = itemStack;
        this.world = world;
        this.entity = entity;
    }

}
