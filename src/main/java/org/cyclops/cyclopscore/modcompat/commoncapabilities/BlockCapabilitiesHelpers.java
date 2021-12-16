package org.cyclops.cyclopscore.modcompat.commoncapabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.cyclops.commoncapabilities.api.capability.block.BlockCapabilities;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

/**
 * Several helpers for combining block and tile capabilities.
 * @author rubensworks
 */
public class BlockCapabilitiesHelpers {

    /**
     * Safely get a capability from a tile or block.
     * The capability of the tile will be checked first,
     * only if it was not found, the block will be checked.
     * @param world The world.
     * @param pos The position of the tile or block providing the capability.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getTileOrBlockCapability(BlockGetter world, BlockPos pos, Direction side,
                                                        Capability<C> capability) {
        LazyOptional<C> instance = BlockEntityHelpers.getCapability(world, pos, side, capability);
        if (!instance.isPresent()) {
            BlockState blockState = world.getBlockState(pos);
            return BlockCapabilities.getInstance().getCapability(blockState, capability, world, pos, side);
        }
        return instance;
    }

    /**
     * Safely get a capability from a tile or block.
     * The capability of the tile will be checked first,
     * only if it was not found, the block will be checked.
     * @param dimPos The world and position.
     * @param side The side to get the capability from.
     * @param capability The capability.
     * @param <C> The capability instance.
     * @return The lazy optional capability.
     */
    public static <C> LazyOptional<C> getTileOrBlockCapability(DimPos dimPos, Direction side, Capability<C> capability) {
        return getTileOrBlockCapability(dimPos.getLevel(true), dimPos.getBlockPos(), side, capability);
    }

}
