package org.cyclops.cyclopscore.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

/**
 * Manager interface for providing implementations for the {@link net.minecraft.block.state.IBlockState} related
 * methodes inside a block.
 * @see org.cyclops.cyclopscore.block.BlockProperty
 * @see org.cyclops.cyclopscore.block.BlockPropertyManagerComponent
 * @author rubensworks
 */
public interface IBlockPropertyManager {

    public int getMetaFromState(IBlockState state);
    public IBlockState getStateFromMeta(int meta);
    public BlockState createDelegatedBlockState();

}
