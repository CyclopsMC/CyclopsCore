package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

/**
 * A dynamic model with a parent that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingChildDynamicItemAndBlockModel extends DelegatingDynamicItemAndBlockModel {

    protected final IBakedModel baseModel;

    public DelegatingChildDynamicItemAndBlockModel(IBakedModel baseModel) {
        super();
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(IBakedModel baseModel, boolean item, IBlockState blockState, EnumFacing facing, long rand) {
        super(item, blockState, facing, rand);
        this.baseModel = baseModel;
    }

}
