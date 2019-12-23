package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
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
    protected final Random rand;
    protected final IModelData modelData;

    protected final ItemStack itemStack;
    protected final World world;
    protected final LivingEntity entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = new Random();
        this.modelData = new ModelDataMap.Builder().build();

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(BlockState blockState, Direction facing, Random rand, IModelData modelData) {
        super(false, false);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
        this.modelData = modelData;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, World world, LivingEntity entity) {
        super(false, true);
        this.blockState = null;
        this.facing = null;
        this.rand = new Random();
        this.modelData = new ModelDataMap.Builder().build();

        this.itemStack = itemStack;
        this.world = world;
        this.entity = entity;
    }

}
