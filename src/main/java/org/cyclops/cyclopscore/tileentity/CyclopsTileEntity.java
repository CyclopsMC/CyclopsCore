package org.cyclops.cyclopscore.tileentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
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
 * A base class for all the tile entities.
 *
 * If you want this tile to update, just implement {@link ITickingTile}.
 * This class has an anti-lag mechanism to send updates with {@link CyclopsTileEntity#sendUpdate()}.
 * Every instance has a continuously looping counter that counts from getUpdateBackoffTicks() to zero.
 * and every time the counter reaches zero, the backoff will be reset and an update packet will be sent
 * if one has been queued.
 * @author rubensworks
 *
 */
public class CyclopsTileEntity extends TileEntity implements INBTProvider, IDirtyMarkListener {

    private static final int UPDATE_BACKOFF_TICKS = 1;

    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this); // TODO: rewrite NBT serialization

    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;
    private final boolean ticking;
    private Map<Pair<Capability<?>, Direction>, LazyOptional<?>> capabilities = Maps.newHashMap();

    public CyclopsTileEntity(TileEntityType<?> type) {
        super(type);
        sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks()); // Random backoff so not all TE's will be updated at once.
        ticking = this instanceof ITickingTile;
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
     * This does the same as {@link CyclopsTileEntity#sendUpdate()} but will
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
     * Override this method instead of {@link CyclopsTileEntity#updateTicking()}.
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
        BlockHelpers.markForUpdate(getWorld(), getPos());
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 1, getNBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tag = packet.getNbtCompound();
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
    public boolean canInteractWith(PlayerEntity entityPlayer) {
        return true;
    }
    
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        writeGeneratedFieldsToNBT(tag);
        return tag;
    }

    /**
     * Write this tile to the given NBT tag that will be attached to an item.
     * By default, {@link #write(CompoundNBT)} will be called.
     * @param tag The tag to write to.
     * @return The written tag.
     */
    public CompoundNBT writeToItemStack(CompoundNBT tag) {
        return this.write(tag);
    }
    
    @Override
    public final void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        read(tag);
    }

    public void read(CompoundNBT tag) {
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
     * {@link CyclopsTileEntity#write(CompoundNBT)} method.
     */
    public CompoundNBT getNBTTagCompound() {
        CompoundNBT tag = new CompoundNBT();
        tag = write(tag);
        return tag;
    }

    @Override
    public CompoundNBT getUpdateTag() {
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
    protected void invalidateCaps() {
        super.invalidateCaps();

        // Invalidate stored capabilities
        for (LazyOptional<?> lazyOptional : this.capabilities.values()) {
            lazyOptional.invalidate();
        }
    }

    /**
     * Add a sideless capability.
     * This can only be called at tile construction time!
     * @param capability The capability type.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilityInternal(Capability<T> capability, LazyOptional<T> value) { // TODO: make something to auto-serialize caps
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
        this.markDirty();
    }

    /**
     * Apply this interface on any tile classes that should tick.
     */
    public static interface ITickingTile extends ITickableTileEntity {

    }

    /**
     * Component to be used together with the {@link Delegate} annotation to enable tile ticking.
     */
    public static class TickingTileComponent implements ITickingTile {

        private final CyclopsTileEntity tile;

        public TickingTileComponent(CyclopsTileEntity tile) {
            this.tile = tile;
        }

        @Override
        public void tick() {
            tile.updateTicking();
        }

    }

}
