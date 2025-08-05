package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;

/**
 * A dynamic model with a parent that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingChildDynamicItemAndBlockModel extends DelegatingDynamicItemAndBlockModel {

    protected final BlockStateModel baseModel;

    public DelegatingChildDynamicItemAndBlockModel(BlockStateModel baseModel) {
        super();
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(BlockStateModel baseModel, BlockAndTintGetter level, BlockState blockState, Direction facing,
                                                   RandomSource rand, ModelData modelData, ChunkSectionLayer renderType) {
        super(level, blockState, facing, rand, modelData, renderType);
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(BlockStateModel baseModel, ItemStack itemStack, Level level, LivingEntity entity) {
        super(itemStack, level, entity);
        this.baseModel = baseModel;
    }

}
