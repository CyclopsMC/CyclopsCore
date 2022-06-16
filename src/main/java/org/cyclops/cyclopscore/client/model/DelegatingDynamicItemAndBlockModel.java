package org.cyclops.cyclopscore.client.model;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A dynamic model that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingDynamicItemAndBlockModel extends DynamicItemAndBlockModel {

    @Nullable
    protected final BlockState blockState;
    @Nullable
    protected final Direction facing;
    protected final RandomSource rand;
    protected final IModelData modelData;

    protected final ItemStack itemStack;
    protected final Level world;
    protected final LivingEntity entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = new ModelDataMap.Builder().build();

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(BlockState blockState, Direction facing, RandomSource rand, IModelData modelData) {
        super(false, false);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
        this.modelData = modelData;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, Level world, LivingEntity entity) {
        super(false, true);
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = new ModelDataMap.Builder().build();

        this.itemStack = itemStack;
        this.world = world;
        this.entity = entity;
    }

}
