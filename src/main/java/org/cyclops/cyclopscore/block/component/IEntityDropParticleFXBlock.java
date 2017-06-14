package org.cyclops.cyclopscore.block.component;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Interface for the {@link ParticleDropBlockComponent}.
 * @author rubensworks
 *
 */
public interface IEntityDropParticleFXBlock {
    /**
     * Called when a random display tick occurs.
     * @param blockState The block state.
     * @param world The world.
     * @param blockPos The position.
     * @param rand Random object.
     */
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random rand);
}
