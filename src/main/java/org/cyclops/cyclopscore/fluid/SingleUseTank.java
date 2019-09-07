package org.cyclops.cyclopscore.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

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

    private Fluid acceptedFluid;

    /**
     * Make a new tank instance.
     * @param capacity The capacity (mB) for the tank.
     */
    public SingleUseTank(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Fluid acceptedFluid = getAcceptedFluid();
    	int filled = 0;
        if (resource.isEmpty()) {
        	filled = 0;
        } else {
        	if (action.execute() && acceptedFluid == null) {
        		acceptedFluid = resource.getFluid();
        	}
        	if (acceptedFluid == null || acceptedFluid == resource.getFluid()) {
                filled = super.fill(resource, action);
            }
        }
        if(filled > 0) {
        	sendUpdate();
        }
        return filled;
    }
    
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack drained = super.drain(maxDrain, action);
    	if (!drained.isEmpty()) {
    		sendUpdate();
    	}
    	return drained;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack drained = super.drain(resource, action);
        if (!drained.isEmpty()) {
            sendUpdate();
        }
        return drained;
    }

    protected void sendUpdate() {

    }

    /**
     * Reset the tank by setting the inner fluid to null.
     */
    public void reset() {
        acceptedFluid = null;
        setValidator(fluidStack -> true);
    }

    /**
     * Set the accepted fluid for this tank.
     * @param fluid The accepted fluid
     */
    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = fluid;
        setValidator(fluidStack -> fluidStack.getFluid() == fluid);
    }

    /**
     * Get the accepted fluid for this tank.
     * @return The accepted fluid.
     */
    public Fluid getAcceptedFluid() {
        return acceptedFluid;
    }

    @Override
    public void writeTankToNBT(CompoundNBT nbt) {
        super.writeTankToNBT(nbt);
        if (acceptedFluid != null)
            nbt.putString(NBT_ACCEPTED_FLUID, acceptedFluid.getRegistryName().toString());
    }

    @Override
    public void readTankFromNBT(CompoundNBT nbt) {
        super.readTankFromNBT(nbt);
        setAcceptedFluid(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(nbt.getString(NBT_ACCEPTED_FLUID))));
    }
    
}
