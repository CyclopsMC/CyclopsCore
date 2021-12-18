package org.cyclops.cyclopscore.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

import java.util.HashMap;
import java.util.Map;

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

    private Map<Pair<Capability<?>, Direction>, LazyOptional<?>> capabilities = Maps.newHashMap();

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
     * When the block entity is loaded or created.
     */
    public void onLoad() {
        if (capabilities instanceof HashMap) {
            capabilities = ImmutableMap.copyOf(capabilities);
        }
    }

    /**
     * Get the NBT tag for this block entity.
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
     * This can only be called at block entity construction time!
     * @param capability The capability type.
     * @param value The capability.
     * @param <T> The capability type.
     */
    public <T> void addCapabilityInternal(Capability<T> capability, LazyOptional<T> value) {
        capabilities.put(Pair.<Capability<?>, Direction>of(capability, null), value);
    }

    /**
     * Add a sided capability.
     * This can only be called at block entity construction time!
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

}
