package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import org.cyclops.cyclopscore.datastructure.DimPos;

import java.util.Optional;

/**
 * @author rubensworks
 */
public interface ICapabilityHelpersNeoForge {

    /**
     * Safely get a capability from a block entity.
     * @param dimPos The dimensional position of the block providing the block entity.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public <T, C> Optional<T> getCapability(DimPos dimPos, BlockCapability<T, C> capability);

    /**
     * Safely get a capability from a block entity.
     * @param dimPos The dimensional position of the block providing the block entity.
     * @param context The context to get the capability from.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public <T, C> Optional<T> getCapability(DimPos dimPos, C context, BlockCapability<T, C> capability);

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, BlockCapability<T, C> capability);

    /**
     * Safely get a capability from a block entity.
     * @param level The level.
     * @param pos The position of the block of the block entity providing the capability.
     * @param context The context to get the capability from.
     * @param capability The capability.
     * @param <T> The capability instance.
     * @param <C> The capability context.
     * @return The lazy optional capability.
     */
    public <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, C context, BlockCapability<T, C> capability) ;

}
