package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nullable;

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
    protected final ModelData modelData;
    protected final RenderType renderType;

    protected final ItemStack itemStack;
    protected final Level world;
    protected final LivingEntity entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = ModelData.EMPTY;
        this.renderType = RenderType.cutout();

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, RenderType renderType) {
        super(false, false);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
        this.modelData = modelData;
        this.renderType = renderType;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, Level world, LivingEntity entity) {
        super(false, true);
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = ModelData.EMPTY;
        this.renderType = RenderType.cutout();

        this.itemStack = itemStack;
        this.world = world;
        this.entity = entity;
    }

}
