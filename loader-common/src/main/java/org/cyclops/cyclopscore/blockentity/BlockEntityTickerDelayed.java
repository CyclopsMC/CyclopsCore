package org.cyclops.cyclopscore.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.helper.IModHelpers;

/**
 * This ticker has an anti-lag mechanism to send updates.
 *
 * Block entities using this ticker must implement {@link IBlockEntityDelayedTickable}.
 *
 * Override {@link BlockEntityTickerDelayed#update(Level, BlockPos, BlockState, BlockEntity)}
 * with the actual update logic.
 *
 * Every instance has a continuously looping counter that counts from getUpdateBackoffTicks() to zero.
 * and every time the counter reaches zero, the backoff will be reset and an update packet will be sent
 * if one has been queued.
 * @author rubensworks
 */
public class BlockEntityTickerDelayed<T extends BlockEntity & IBlockEntityDelayedTickable> implements BlockEntityTicker<T> {

    /**
     * Do not override this method (you won't even be able to do so).
     * Use update() instead.
     * @param level The level.
     * @param pos The position.
     * @param blockState The block state.
     * @param blockEntity The block entity.
     */
    @Override
    public final void tick(Level level, BlockPos pos, BlockState blockState, T blockEntity) {
        update(level, pos, blockState, blockEntity);
        trySendActualUpdate(level, pos, blockEntity);
    }

    /**
     * Override this method instead of {@link BlockEntityTickerDelayed#tick(Level, BlockPos, BlockState, BlockEntity)}.
     * This method is called each tick.
     */
    protected void update(Level level, BlockPos pos, BlockState blockState, T blockEntity) {

    }

    private void trySendActualUpdate(Level level, BlockPos pos, T blockEntity) {
        blockEntity.reduceUpdateBackoff();
        if(blockEntity.getUpdateBackoff() <= 0) {
            blockEntity.setUpdateBackoff(blockEntity.getUpdateBackoffTicks());

            if(blockEntity.shouldSendUpdate()) {
                blockEntity.unsetSendUpdate();

                beforeSendUpdate();
                onSendUpdate(level, pos);
                afterSendUpdate();
            }
        }
    }

    /**
     * Called when an update will is sent.
     * This contains the logic to send the update, so make sure to call the super!
     * @param level The level.
     * @param pos The position.
     */
    protected void onSendUpdate(Level level, BlockPos pos) {
        IModHelpers.get().getBlockHelpers().markForUpdate(level, pos);
    }

    /**
     * Called when before update is sent.
     */
    protected void beforeSendUpdate() {

    }

    /**
     * Called when after update is sent. (Not necessarily received yet!)
     */
    protected void afterSendUpdate() {

    }
}
