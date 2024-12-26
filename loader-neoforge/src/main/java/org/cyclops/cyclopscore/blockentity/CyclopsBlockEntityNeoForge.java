package org.cyclops.cyclopscore.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A base class for all the block entities.
 *
 * @author rubensworks
 */
public class CyclopsBlockEntityNeoForge extends CyclopsBlockEntity {

    public CyclopsBlockEntityNeoForge(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, packet, lookupProvider);
        CompoundTag tag = packet.getTag();
        read(tag, lookupProvider);
        onUpdateReceived();
    }

    /**
     * This method is called when the block entity receives
     * an update (ie a data packet) from the server.
     * If this block entity uses NBT, then the NBT will have
     * already been updated when this method is called.
     */
    public void onUpdateReceived() {

    }
}
