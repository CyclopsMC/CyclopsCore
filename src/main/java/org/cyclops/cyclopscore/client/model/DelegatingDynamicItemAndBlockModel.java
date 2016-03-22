package org.cyclops.cyclopscore.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * A dynamic model that can be used for items and blocks that delegates its blockstate.
 * @author rubensworks
 */
public abstract class DelegatingDynamicItemAndBlockModel extends DynamicItemAndBlockModel {

    protected final IBlockState blockState;
    protected final EnumFacing facing;
    protected final long rand;

    public DelegatingDynamicItemAndBlockModel() {
        super(true, false);
        this.blockState = null;
        this.facing = null;
        this.rand = 0;
    }

    public DelegatingDynamicItemAndBlockModel(boolean item, IBlockState blockState, EnumFacing facing, long rand) {
        super(false, item);
        this.blockState = blockState;
        this.facing = facing;
        this.rand = rand;
    }

}
