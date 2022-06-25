package org.cyclops.cyclopscore.block.component;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
    public void randomDisplayTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource rand);
}
