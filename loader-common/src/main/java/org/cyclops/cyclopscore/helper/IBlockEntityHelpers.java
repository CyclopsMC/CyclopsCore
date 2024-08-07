package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author rubensworks
 */
public interface IBlockEntityHelpers {

    /**
     * If block entities should be retrieved in an unsafe manner from the non-main thread.
     * USE WITH CAUTION!!!
     */
    public boolean isUnsafeBlockEntityGetter();

    /**
     * If block entities should be retrieved in an unsafe manner from the non-main thread.
     * USE WITH CAUTION!!!
     */
    public void setUnsafeBlockEntityGetter(boolean unsafeBlockEntityGetter);

    /**
     * Safely cast a block entity.
     * @param level The level.
     * @param pos The position of the block providing the block entity.
     * @param targetClazz The class to cast to.
     * @param <T> The type of block entity to cast at.
     * @return The optional block entity.
     */
    public <T> Optional<T> get(BlockGetter level, BlockPos pos, Class<T> targetClazz);

    /* WARNING: Hack to allow block entities to be retrieved from other threads. Needed for our IngredientObserver. */
    /* This is just a copy of {@link Level#getBlockEntity} without the thread checks. */
    @Nullable
    public BlockEntity getLevelBlockEntityUnchecked(Level level, BlockPos pos);

}
