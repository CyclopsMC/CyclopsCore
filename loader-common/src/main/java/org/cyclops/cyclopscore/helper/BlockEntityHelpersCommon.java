package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author rubensworks
 */
public class BlockEntityHelpersCommon implements IBlockEntityHelpers {

    private boolean unsafeBlockEntityGetter = false;

    @Override
    public boolean isUnsafeBlockEntityGetter() {
        return this.unsafeBlockEntityGetter;
    }

    @Override
    public void setUnsafeBlockEntityGetter(boolean unsafeBlockEntityGetter) {
        this.unsafeBlockEntityGetter = unsafeBlockEntityGetter;
    }

    @Override
    public <T> Optional<T> get(BlockGetter level, BlockPos pos, Class<T> targetClazz) {
        BlockEntity blockEntity = isUnsafeBlockEntityGetter() && level instanceof Level && !((Level) level).isClientSide() && Thread.currentThread() != ((Level) level).getServer().getRunningThread() ? getLevelBlockEntityUnchecked((Level) level, pos) : level.getBlockEntity(pos);
        if (blockEntity == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(targetClazz.cast(blockEntity));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Nullable
    @Override
    public BlockEntity getLevelBlockEntityUnchecked(Level level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return null;
        } else {
            return level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
        }
    }
}
