package org.cyclops.cyclopscore.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A base class for all the block entities.
 *
 * If you want this tile to update, just implement {@link ITickingBlockEntity}.
 * This class has an anti-lag mechanism to send updates with {@link CyclopsBlockEntity#sendUpdate()}.
 * Every instance has a continuously looping counter that counts from getUpdateBackoffTicks() to zero.
 * and every time the counter reaches zero, the backoff will be reset and an update packet will be sent
 * if one has been queued.
 * @author rubensworks
 *
 */
public class CyclopsBlockEntity extends BlockEntity implements INBTProvider, IDirtyMarkListener {

    private static final int UPDATE_BACKOFF_TICKS = 1;

    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);

    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;
    private final boolean ticking;
    private Map<Pair<Capability<?>, Direction>, LazyOptional<?>> capabilities = Maps.newHashMap();

    public CyclopsBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks()); // Random backoff so not all TE's will be updated at once.
        ticking = this instanceof ITickingBlockEntity;
    }

    protected boolean isTicking() {
        return ticking;
    }

    /**
     * Send a world update for the coordinates of this tile entity.
     * This will always send lag-safe updates, so calling this many times per tick will
     * not cause multiple packets to be sent, more info in the class javadoc.
     */
    public final void sendUpdate() {
        if(!isTicking()) {
            throw new RuntimeException("If you want to update, you must implement ITickingTile. This is a mod error.");
        }
        shouldSendUpdate = true;
    }

    /**
     * Send an immediate world update for the coordinates of this tile entity.
     * This does the same as {@link CyclopsBlockEntity#sendUpdate()} but will
     * ignore the update backoff.
     */
    public final void sendImmediateUpdate() {
        sendUpdate();
        sendUpdateBackoff = 0;
    }

    /**
     * Do not override this method (you won't even be able to do so).
     * Use updateTileEntity() instead.
     */
    private void updateTicking() {
        updateTileEntity();
        trySendActualUpdate();
    }

    /**
     * Override this method instead of {@link CyclopsBlockEntity#updateTicking()}.
     * This method is called each tick.
     */
    protected void updateTileEntity() {

    }

    private void trySendActualUpdate() {
        sendUpdateBackoff--;
        if(sendUpdateBackoff <= 0) {
            sendUpdateBackoff = getUpdateBackoffTicks();

            if(shouldSendUpdate) {
                shouldSendUpdate = false;

                beforeSendUpdate();
                onSendUpdate();
                afterSendUpdate();
            }
        }
    }

    /**
     * Called when an update will is sent.
     * This contains the logic to send the update, so make sure to call the super!
     */
    protected void onSendUpdate() {
        BlockHelpers.markForUpdate(getLevel(), getBlockPos());
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
     * This method is called when the tile entity receives
     * an update (ie a data packet) from the server.
     * If this tile entity  uses NBT, then the NBT will have
     * already been updated when this method is called.
     */
    public void onUpdateReceived() {

    }

    /**
     * @return The minimum amount of ticks between two consecutive sent packets.
     */
    protected int getUpdateBackoffTicks() {
        return UPDATE_BACKOFF_TICKS;
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
     * Write this tile to the given NBT tag that will be attached to an item.
     * By default, {@link #save(CompoundTag)}} will be called.
     * @param tag The tag to write to.
     * @return The written tag.
     */
    public CompoundTag writeToItemStack(CompoundTag tag) {
        return this.save(tag);
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

    /**
     * When the tile is loaded or created.
     */
    public void onLoad() {
        if (capabilities instanceof HashMap) {
            capabilities = ImmutableMap.copyOf(capabilities);
        }
    }
    
    /**
     * Get the NBT tag for this tile entity.
     * @return The NBT tag that is created with the
     * {@link CyclopsBlockEntity#save(CompoundTag)} method.
     */
    public CompoundTag getNBTTagCompound() {
        CompoundTag tag = new CompoundTag();
        tag = save(tag);
        return tag;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return getNBTTagCompound();
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
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        if (capabilities != null) {
            LazyOptional<?> value = capabilities.get(Pair.<Capability<?>,
                    Direction>of(capability, transformFacingForRotation(facing)));
            if (value == null && facing != null) {
                value = capabilities.get(Pair.<Capability<?>, Direction>of(capability, null));
            }
            if (value != null) {
                return value.cast();
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }

    /**
     * Add a sideless capability.
     * This can only be called at tile construction time!
     * @param capability The capability type.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilityInternal(Capability<T> capability, LazyOptional<T> value) {
        capabilities.put(Pair.<Capability<?>, Direction>of(capability, null), value);
    }

    /**
     * Add a sided capability.
     * This can only be called at tile construction time!
     * @param capability The capability type.
     * @param facing The side for the capability.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilitySided(Capability<T> capability, Direction facing, LazyOptional<T> value) {
        capabilities.put(Pair.<Capability<?>, Direction>of(capability, facing), value);
    }

    protected Map<Pair<Capability<?>, Direction>, LazyOptional<?>> getStoredCapabilities() {
        return capabilities;
    }

    @Override
    public void onDirty() {
        this.setChanged();
    }

    /**
     * Apply this interface on any tile classes that should tick.
     */
    public static interface ITickingBlockEntity extends TickingBlockEntity {

    }

    /**
     * Component to be used together with the {@link Delegate} annotation to enable tile ticking.
     */
    public static class TickingTileComponent implements ITickingBlockEntity {

        private final CyclopsBlockEntity tile;

        public TickingTileComponent(CyclopsBlockEntity tile) {
            this.tile = tile;
        }

        @Override
        public void tick() {
            tile.updateTicking();
        }

        @Override
        public boolean isRemoved() {
            return tile.isRemoved();
        }

        @Override
        public BlockPos getPos() {
            return tile.getBlockPos();
        }

        @Override
        public String getType() {
            return tile.getType().toString();
        }

    }

}
