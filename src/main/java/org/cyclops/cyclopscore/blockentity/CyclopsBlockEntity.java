package org.cyclops.cyclopscore.blockentity;

import lombok.experimental.Delegate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

/**
 * A base class for all the block entities.
 * @author rubensworks
 */
public class CyclopsBlockEntity extends BlockEntity implements INBTProvider, IDirtyMarkListener, IBlockEntityDelayedTickable {

    private static final int UPDATE_BACKOFF_TICKS = 1;

    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);
    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;

    public CyclopsBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        // Random backoff so not all block entities will be updated at once.
        sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks());

        CyclopsCore._instance.getModEventBus().addListener(this::registerCapabilities);
    }

    protected void registerCapabilities(RegisterCapabilitiesEvent event) {

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

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(net, packet);
        CompoundTag tag = packet.getTag();
        read(tag);
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

    /**
     * If this entity is interactable with a player.
     * @param entityPlayer The player that is checked.
     * @return If the given player can interact.
     */
    public boolean canInteractWith(Player entityPlayer) {
        return true;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        writeGeneratedFieldsToNBT(tag);
    }

    /**
     * Write this block entity to the given NBT tag that will be attached to an item.
     * By default, {@link #saveAdditional(CompoundTag)}} will be called.
     * @param tag The tag to write to.
     * @return The written tag.
     */
    public CompoundTag writeToItemStack(CompoundTag tag) {
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public final void load(CompoundTag tag) {
        super.load(tag);
        read(tag);
    }

    public void read(CompoundTag tag) {
        readGeneratedFieldsFromNBT(tag);
        onLoad();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
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

}
