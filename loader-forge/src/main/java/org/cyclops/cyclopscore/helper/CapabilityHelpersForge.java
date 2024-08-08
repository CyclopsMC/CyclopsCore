package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

/**
 * @author rubensworks
 */
public class CapabilityHelpersForge implements ICapabilityHelpersForge {

    private final IModHelpersForge modHelpers;

    public CapabilityHelpersForge(IModHelpersForge modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public <C> LazyOptional<C> getCapability(BlockGetter level, BlockPos pos, Capability<C> capability) {
        return getCapability(level, pos, null, capability);
    }

    @Override
    public <C> LazyOptional<C> getCapability(BlockGetter level, BlockPos pos, Direction side, Capability<C> capability) {
        BlockEntity blockEntity = modHelpers.getBlockEntityHelpers().get(level, pos, BlockEntity.class).orElse(null);
        if(blockEntity != null) {
            return blockEntity.getCapability(capability, side);
        }
        return LazyOptional.empty();
    }
}
