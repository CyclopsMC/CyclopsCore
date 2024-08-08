package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * @author rubensworks
 */
public interface ICapabilityHelpersForge {

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public <C> LazyOptional<C> getCapability(BlockGetter level, BlockPos pos, Capability<C> capability);

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public <C> LazyOptional<C> getCapability(BlockGetter level, BlockPos pos, Direction side, Capability<C> capability);

}
