package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

/**
 * A dynamic model with a parent that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingChildDynamicItemAndBlockModel extends DelegatingDynamicItemAndBlockModel {

    protected final ResolvedModel baseModel;

    public DelegatingChildDynamicItemAndBlockModel(ResolvedModel baseModel) {
        super();
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(ResolvedModel baseModel, BlockState blockState, Direction facing,
                                                   RandomSource rand, ModelData modelData, RenderType renderType) {
        super(blockState, facing, rand, modelData, renderType);
        this.baseModel = baseModel;
    }

    @Override
    public UnbakedModel wrapped() {
        return this.baseModel.wrapped();
    }

    @Override
    public @Nullable ResolvedModel parent() {
        return this.baseModel;
    }

    @Override
    public String debugName() {
        return this.baseModel.debugName();
    }

}
