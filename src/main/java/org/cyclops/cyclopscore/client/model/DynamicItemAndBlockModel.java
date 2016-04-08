package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

/**
 * A dynamic model that can be used for items and blocks.
 * @author rubensworks
 */
public abstract class DynamicItemAndBlockModel extends DynamicBaseModel {

    private final boolean factory;
    private final boolean item;
    private final ItemOverrides itemOverrides;

    public DynamicItemAndBlockModel(boolean factory, boolean item) {
        this.factory = factory;
        this.item = item;
        this.itemOverrides = new ItemOverrides();
    }

    protected boolean isItemStack() {
        return item;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        if(factory) {
            IBakedModel bakedModel;
            if(isItemStack()) {
                bakedModel = handleItemState(null, null, null);
            } else {
                bakedModel = handleBlockState(state, side, rand);
            }
            return bakedModel.getQuads(state, side, rand);
        }
        return getGeneralQuads();
    }

    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    public abstract IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand);
    public abstract IBakedModel handleItemState(ItemStack stack, World world, EntityLivingBase entity);

    @Override
    public ItemOverrideList getOverrides() {
        return itemOverrides;
    }

    public class ItemOverrides extends ItemOverrideList {

        public ItemOverrides() {
            super(Collections.<ItemOverride>emptyList());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            return DynamicItemAndBlockModel.this.handleItemState(stack, world, entity);
        }
    }

}
