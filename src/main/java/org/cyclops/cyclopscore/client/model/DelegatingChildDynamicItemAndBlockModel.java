package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

/**
 * A dynamic model with a parent that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingChildDynamicItemAndBlockModel extends DelegatingDynamicItemAndBlockModel {

    protected final BakedModel baseModel;

    public DelegatingChildDynamicItemAndBlockModel(BakedModel baseModel) {
        super();
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(BakedModel baseModel, BlockState blockState, Direction facing,
                                                   RandomSource rand, IModelData modelData) {
        super(blockState, facing, rand, modelData);
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(BakedModel baseModel, ItemStack itemStack, Level world, LivingEntity entity) {
        super(itemStack, world, entity);
        this.baseModel = baseModel;
    }

}
