package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A dynamic model that can be used for items and blocks.
 * @author rubensworks
 */
public abstract class DynamicItemAndBlockModel extends DynamicBaseModel {

    private final boolean factory;
    private final boolean item;
    private final ItemOverrides itemOverrides;

    private Direction renderingSide;

    public DynamicItemAndBlockModel(boolean factory, boolean item) {
        this.factory = factory;
        this.item = item;
        this.itemOverrides = new ItemOverrides();
    }

    protected boolean isItemStack() {
        return item;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                    @Nonnull Random rand, @Nonnull IModelData extraData) {
        this.renderingSide = side;
        if(factory) {
            IBakedModel bakedModel;
            if(isItemStack()) {
                bakedModel = handleItemState(null, null, null);
            } else {
                bakedModel = handleBlockState(state, side, rand, extraData);
            }
            if (bakedModel != null) {
                return bakedModel.getQuads(state, side, rand);
            }
        }
        return getGeneralQuads();
    }

    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    public abstract IBakedModel handleBlockState(@Nullable BlockState state, @Nullable Direction side,
                                                 @Nonnull Random rand, @Nonnull IModelData extraData);
    public abstract IBakedModel handleItemState(@Nullable ItemStack stack, @Nullable World world,
                                                @Nullable LivingEntity entity);

    @Override
    public ItemOverrideList getOverrides() {
        return itemOverrides;
    }

    public Direction getRenderingSide() {
        return renderingSide;
    }

    public class ItemOverrides extends ItemOverrideList {
        @Nullable
        @Override
        public IBakedModel resolve(IBakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
            return DynamicItemAndBlockModel.this.handleItemState(stack, world, entity);
        }
    }

}
