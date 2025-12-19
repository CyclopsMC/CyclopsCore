package org.cyclops.cyclopscore.fluid;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;

import java.util.List;
import java.util.Objects;

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
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (acceptedFluid != Fluids.EMPTY && !resource.is(acceptedFluid)) {
            return 0;
        }
        return super.insert(index, resource, amount, transaction);
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        super.onContentsChanged(index, previousContents);
        if (getFluid() != previousContents) {
            sendUpdate();
        }
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
    }

    /**
     * Set the accepted fluid for this tank.
     * @param fluid The accepted fluid
     */
    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = Objects.requireNonNull(fluid);
    }

    /**
     * Get the accepted fluid for this tank.
     * @return The accepted fluid.
     */
    public Fluid getAcceptedFluid() {
        return acceptedFluid;
    }

    @Override
    public void serializeTank(ValueOutput output) {
        super.serializeTank(output);
        output.putString(NBT_ACCEPTED_FLUID, BuiltInRegistries.FLUID.getKey(acceptedFluid).toString());
    }

    @Override
    public void deserializeTank(ValueInput input) {
        super.deserializeTank(input);
        setAcceptedFluid(BuiltInRegistries.FLUID.getValue(Identifier.parse(input.getString(NBT_ACCEPTED_FLUID).orElseThrow())));
    }

}
