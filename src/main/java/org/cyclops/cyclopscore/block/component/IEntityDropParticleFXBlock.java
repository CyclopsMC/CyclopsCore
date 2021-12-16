package org.cyclops.cyclopscore.block.component;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
    public void randomDisplayTick(BlockState blockState, Level world, BlockPos blockPos, Random rand);
}
