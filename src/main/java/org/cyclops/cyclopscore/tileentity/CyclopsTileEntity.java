package org.cyclops.cyclopscore.tileentity;

import com.google.common.collect.*;
import lombok.experimental.Delegate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

import javax.annotation.Nonnull;
import java.util.IdentityHashMap;
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
public class CyclopsTileEntity extends TileEntity implements INBTProvider {

    private static final int UPDATE_BACKOFF_TICKS = 1;

    @NBTPersist
    private Boolean rotatable = false;
    private EnumFacing rotation = EnumFacing.NORTH;
    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);

    private boolean shouldSendUpdate = false;
    private int sendUpdateBackoff = 0;
    private final boolean ticking;
    @SuppressWarnings("unchecked")
    private Map<Capability<?>, Object> innerCapabilities = new IdentityHashMap();
    private Table<Capability<?>, EnumFacing, Object> sidedCapabilities = HashBasedTable.create();

    public CyclopsTileEntity() {
        sendUpdateBackoff = (int) Math.round(Math.random() * getUpdateBackoffTicks()); // Random backoff so not all TE's will be updated at once.
        ticking = this instanceof ITickingTile;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
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
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 1, getNBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        NBTTagCompound tag = packet.getNbtCompound();
        readFromNBT(tag);
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
     * Called when the blockState of this tile entity is destroyed.
     */
    public void destroy() {
        invalidate();
    }
    
    /**
     * If this entity is interactable with a player.
     * @param entityPlayer The player that is checked.
     * @return If the given player can interact.
     */
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        writeGeneratedFieldsToNBT(tag);
        
        // Separate action for direction
        tag.setInteger("rotation", rotation.ordinal());
        return tag;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readGeneratedFieldsFromNBT(tag);
        
        // Separate action for direction
        EnumFacing foundRotation = EnumFacing.VALUES[tag.getInteger("rotation")];
        if(foundRotation != null) {
        	rotation = foundRotation;
        }
        onLoad();
    }

    /**
     * When the tile is loaded or created.
     */
    public void onLoad() {
        if (sidedCapabilities instanceof HashBasedTable) {
            sidedCapabilities = ImmutableTable.copyOf(sidedCapabilities);
        }
    }
    
    /**
     * Get the NBT tag for this tile entity.
     * @return The NBT tag that is created with the
     * {@link CyclopsTileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)} method.
     */
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag = writeToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return getNBTTagCompound();
    }

    /**
     * If the blockState this tile entity has can be rotated.
     * @return If it can be rotated.
     */
    public boolean isRotatable() {
        return this.rotatable;
    }

    /**
     * Set whether or not the blockState that has this tile entity can be rotated.
     * @param rotatable If it can be rotated.
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    /**
     * Get the current rotation of this tile entity.
     * Default is {@link net.minecraft.util.EnumFacing#NORTH}.
     * @return The rotation.
     */
    public EnumFacing getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of this tile entity.
     * Default is {@link net.minecraft.util.EnumFacing#NORTH}.
     * @param rotation The new rotation.
     */
    public void setRotation(EnumFacing rotation) {
        this.rotation = rotation;
    }
    
    /**
     * Get the blockState type this tile entity is defined for.
     * @return The blockState instance.
     */
    public ConfigurableBlockContainer getBlock() {
        return (ConfigurableBlockContainer) this.getBlockType();
    }

    protected EnumFacing transformFacingForRotation(EnumFacing facing) {
        if (facing == null) {
            return null;
        }
        return DirectionHelpers.transformFacingForRotation(facing, getRotation());
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        if (facing != null){
            if (sidedCapabilities.contains(capability, transformFacingForRotation(facing)))
                return true;
        }
        return innerCapabilities.containsKey(capability) || super.hasCapability(capability, facing);

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        Object value;
        if (facing != null){
            value = sidedCapabilities.get(capability, facing);
        }
        else value = innerCapabilities.get(capability);
        if (value != null)
            return (T) value;

        return super.getCapability(capability, facing);
    }

    /**
     * Add a sideless capability.
     * This can only be called at tile construction time!
     * @param capability The capability type.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilityInternal(Capability<T> capability, T value) {
        innerCapabilities.put(capability, value);
    }

    /**
     * Add a sided capability.
     * This can only be called at tile construction time!
     * @param capability The capability type.
     * @param facing The side for the capability.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilitySided(Capability<T> capability, EnumFacing facing, T value) {
        sidedCapabilities.put(capability, facing, value);
    }

    protected Table<Capability<?>, EnumFacing, Object> getSidedCapabilities() {
        return sidedCapabilities;
    }

    protected Map<Capability<?>, Object> getInnerCapabilities() {
        return innerCapabilities;
    }

    /**
     * Apply this interface on any tile classes that should tick.
     */
    public static interface ITickingTile extends ITickable {

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
        public void update() {
            tile.updateTicking();
        }

    }

}
