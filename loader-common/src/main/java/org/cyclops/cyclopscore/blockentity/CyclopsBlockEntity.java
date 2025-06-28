package org.cyclops.cyclopscore.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

/**
 * A base class for all the block entities.
 *
 * @author rubensworks
 */
public class CyclopsBlockEntity extends BlockEntity implements INBTProvider, IDirtyMarkListener, IBlockEntityDelayedTickable {

    private static final int UPDATE_BACKOFF_TICKS = 1;

    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);
    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;

    public CyclopsBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        // Random backoff so not all block entities will be updated at once.
        sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks());
    }

    @Override
    public int getUpdateBackoffTicks() {
        return UPDATE_BACKOFF_TICKS;
    }

    @Override
    public void sendUpdate() {
        shouldSendUpdate = true;
    }

    @Override
    public boolean shouldSendUpdate() {
        return this.shouldSendUpdate;
    }

    @Override
    public void unsetSendUpdate() {
        this.shouldSendUpdate = false;
    }

    @Override
    public void setUpdateBackoff(int updateBackoff) {
        this.sendUpdateBackoff = updateBackoff;
    }

    @Override
    public int getUpdateBackoff() {
        return this.sendUpdateBackoff;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * If this entity is interactable with a player.
     *
     * @param entityPlayer The player that is checked.
     * @return If the given player can interact.
     */
    public boolean canInteractWith(Player entityPlayer) {
        return true;
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        writeGeneratedFieldsToNBT(output);
    }

    /**
     * Write this block entity to the given NBT tag that will be attached to an item.
     * By default, {@link #saveAdditional(ValueOutput)} will be called.
     *
     * @param output The tag to write to.
     */
    public void writeToItemStack(ValueOutput output) {
        this.saveAdditional(output);
    }

    @Override
    public final void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        read(input);
    }

    public void read(ValueInput input) {
        readGeneratedFieldsFromNBT(input);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    protected Direction transformFacingForRotation(Direction facing) {
        if (facing == null) {
            return null;
        }
        if (getRotation() == null) {
            return facing;
        }
        return DirectionHelpers.transformFacingForRotation(facing, getRotation());
    }

    public Direction getRotation() {
        return null;
    }

    @Override
    public void onDirty() {
        this.setChanged();
    }

    @Override
    public void writeGeneratedFieldsToNBT(ValueOutput output) {
        this.nbtProviderComponent.writeGeneratedFieldsToNBT(output);
    }

    @Override
    public void readGeneratedFieldsFromNBT(ValueInput input) {
        this.nbtProviderComponent.readGeneratedFieldsFromNBT(input);
    }
}
