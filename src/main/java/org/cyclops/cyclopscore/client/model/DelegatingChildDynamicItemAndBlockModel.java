package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;

import java.util.Random;

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

    public DelegatingChildDynamicItemAndBlockModel(IBakedModel baseModel, BlockState blockState, Direction facing,
                                                   Random rand, IModelData modelData) {
        super(blockState, facing, rand, modelData);
        this.baseModel = baseModel;
    }

    public DelegatingChildDynamicItemAndBlockModel(IBakedModel baseModel, ItemStack itemStack, World world, LivingEntity entity) {
        super(itemStack, world, entity);
        this.baseModel = baseModel;
    }

}
