package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A dynamic model that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingDynamicItemAndBlockModel extends DynamicItemAndBlockModel {

    protected final IBlockState blockState;
    protected final EnumFacing facing;
    protected final long rand;

    protected final ItemStack itemStack;
    protected final World world;
    protected final EntityLivingBase entity;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = 0;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(IBlockState blockState, EnumFacing facing, long rand) {
        super(false, false);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;

        this.itemStack = null;
        this.world = null;
        this.entity = null;
    }

    public DelegatingDynamicItemAndBlockModel(ItemStack itemStack, World world, EntityLivingBase entity) {
        super(false, true);
        this.blockState = null;
        this.facing = null;
        this.rand = 0L;

        this.itemStack = itemStack;
        this.world = world;
        this.entity = entity;
    }

}
