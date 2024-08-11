package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import org.cyclops.cyclopscore.datastructure.DimPos;

import java.util.Optional;

/**
 * @author rubensworks
 */
public class CapabilityHelpersNeoForge implements ICapabilityHelpersNeoForge {

    private final IModHelpers modHelpers;

    public CapabilityHelpersNeoForge(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public <T, C> Optional<T> getCapability(DimPos dimPos, BlockCapability<T, C> capability) {
        Level level = dimPos.getLevel(true);
        if (level == null) {
            return Optional.empty();
        }
        return getCapability(level, dimPos.getBlockPos(), null, capability);
    }

    @Override
    public <T, C> Optional<T> getCapability(DimPos dimPos, C context, BlockCapability<T, C> capability) {
        Level level = dimPos.getLevel(true);
        if (level == null) {
            return Optional.empty();
        }
        return getCapability(level, dimPos.getBlockPos(), context, capability);
    }

    @Override
    public <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, BlockCapability<T, C> capability) {
        return getCapability(level, pos, null, capability);
    }

    @Override
    public <T, C> Optional<T> getCapability(ILevelExtension level, BlockPos pos, C context, BlockCapability<T, C> capability) {
        Level levelFull = (Level) level;
        BlockState state = levelFull.getBlockState(pos);
        BlockEntity blockEntity = state.hasBlockEntity() ? this.modHelpers.getBlockEntityHelpers().get(levelFull, pos, BlockEntity.class).orElse(null) : null;
        return Optional.ofNullable(level.getCapability(capability, pos, state, blockEntity, context));
    }
}
