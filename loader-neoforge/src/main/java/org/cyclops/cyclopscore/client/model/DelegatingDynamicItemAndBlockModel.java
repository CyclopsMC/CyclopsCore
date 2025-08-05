package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;

import javax.annotation.Nullable;

/**
 * A dynamic model that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingDynamicItemAndBlockModel extends DynamicItemAndBlockModel {

    @Nullable
    protected final BlockAndTintGetter level;
    @Nullable
    protected final BlockState blockState;
    @Nullable
    protected final Direction facing;
    protected final RandomSource rand;
    protected final ModelData modelData;
    protected final ChunkSectionLayer renderType;
    @Nullable
    protected final ItemStack itemStack;
    @Nullable
    protected final LivingEntity entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.level = null;
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = ModelData.EMPTY;
        this.renderType = ChunkSectionLayer.CUTOUT;
        this.itemStack = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(BlockAndTintGetter level, BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, ChunkSectionLayer renderType) {
        super(false, false);
        this.level = level;
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
        this.modelData = modelData;
        this.renderType = renderType;
        this.itemStack = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, Level level, LivingEntity entity) {
        super(false, true);
        this.level = level;
        this.blockState = null;
        this.facing = null;
        this.rand = RandomSource.create();
        this.modelData = ModelData.EMPTY;
        this.renderType = ChunkSectionLayer.CUTOUT;
        this.itemStack = itemStack;
        this.entity = entity;
    }

}
