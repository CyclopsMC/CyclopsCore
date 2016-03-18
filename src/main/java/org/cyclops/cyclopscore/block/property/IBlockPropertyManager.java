package org.cyclops.cyclopscore.block.property;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

/**
 * Manager interface for providing implementations for the {@link net.minecraft.block.state.IBlockState} related
 * methodes inside a block.
 * @see BlockProperty
 * @see BlockPropertyManagerComponent
 * @author rubensworks
 */
public interface IBlockPropertyManager {

    public int getMetaFromState(IBlockState state);
    public IBlockState getStateFromMeta(int meta);
    public BlockStateContainer createDelegatedBlockState();

}
