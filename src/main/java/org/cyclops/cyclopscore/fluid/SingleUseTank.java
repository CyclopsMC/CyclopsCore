package org.cyclops.cyclopscore.fluid;

import com.google.common.collect.Lists;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import java.util.List;
import java.util.Objects;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * A simple tank that can accept and drain fluids until the capacity is reached.
 * Only one fluid can be accepted, which must be specified with {@link SingleUseTank#setAcceptedFluid(Fluid)}.
 * Based on the Buildcraft SingleUseTank.
 *
 * @author rubensworks
 *
 */
public class SingleUseTank extends Tank {

    /**
     * The NBT name for the fluid tank.
     */
    public static final String NBT_ACCEPTED_FLUID = "acceptedFluid";

    private final List<IDirtyMarkListener> dirtyMarkListeners = Lists.newLinkedList();

    private Fluid acceptedFluid;

    /**
     * Make a new tank instance.
     * @param capacity The capacity (mB) for the tank.
     */
    public SingleUseTank(int capacity) {
        super(capacity);
        setAcceptedFluid(Fluids.EMPTY);
    }

    /**
     * Add a dirty marking listener.
     * @param dirtyMarkListener The dirty mark listener.
     */
    public synchronized void addDirtyMarkListener(IDirtyMarkListener dirtyMarkListener) {
        this.dirtyMarkListeners.add(dirtyMarkListener);
    }

    /**
     * Remove a dirty marking listener.
     * @param dirtyMarkListener The dirty mark listener.
     */
    public synchronized void removeDirtyMarkListener(IDirtyMarkListener dirtyMarkListener) {
        this.dirtyMarkListeners.remove(dirtyMarkListener);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Fluid acceptedFluid = getAcceptedFluid();
        int filled = 0;
        if (resource.isEmpty()) {
            filled = 0;
        } else {
            if (action.execute() && acceptedFluid == Fluids.EMPTY) {
                acceptedFluid = resource.getFluid();
            }
            if (acceptedFluid == Fluids.EMPTY || acceptedFluid == resource.getFluid()) {
                filled = super.fill(resource, action);
            }
        }
        if(action.execute() && filled > 0) {
            sendUpdate();
        }
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack drained = super.drain(maxDrain, action);
        if (action.execute() && !drained.isEmpty()) {
            sendUpdate();
        }
        return drained;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack drained = super.drain(resource, action);
        if (action.execute() && !drained.isEmpty()) {
            sendUpdate();
        }
        return drained;
    }

    protected void sendUpdate() {
        List<IDirtyMarkListener> dirtyMarkListeners;
        synchronized (this) {
            dirtyMarkListeners = Lists.newLinkedList(this.dirtyMarkListeners);
        }
        for(IDirtyMarkListener dirtyMarkListener : dirtyMarkListeners) {
            dirtyMarkListener.onDirty();
        }
    }

    /**
     * Reset the tank by setting the inner fluid to null.
     */
    public void reset() {
        acceptedFluid = Fluids.EMPTY;
        setValidator(fluidStack -> true);
    }

    /**
     * Set the accepted fluid for this tank.
     * @param fluid The accepted fluid
     */
    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = Objects.requireNonNull(fluid);
        if (fluid == Fluids.EMPTY) {
            setValidator(fluidStack -> true);
        } else {
            setValidator(fluidStack -> fluidStack.getFluid() == fluid);
        }
    }

    /**
     * Get the accepted fluid for this tank.
     * @return The accepted fluid.
     */
    public Fluid getAcceptedFluid() {
        return acceptedFluid;
    }

    @Override
    public void writeTankToNBT(CompoundTag nbt) {
        super.writeTankToNBT(nbt);
        nbt.putString(NBT_ACCEPTED_FLUID, ForgeRegistries.FLUIDS.getKey(acceptedFluid).toString());
    }

    @Override
    public void readTankFromNBT(CompoundTag nbt) {
        super.readTankFromNBT(nbt);
        setAcceptedFluid(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(nbt.getString(NBT_ACCEPTED_FLUID))));
    }

}
