package org.cyclops.cyclopscore.fluid;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A simple tank that can accept and drain fluids until the capacity is reached.
 * Only one fluid can be accepted, which must be specified with {@link SingleUseTank#setAcceptedFluid(Fluid)}.
 * Based on the Buildcraft SingleUseTank.
 *
 * Implement {@link IUpdateListener} on the given tile
 * to make it listen to tank changes.
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
     * @param tile The TileEntity that uses this tank.
     */
    public SingleUseTank(int capacity, TileEntity tile) {
        super(capacity, tile);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        Fluid acceptedFluid = getAcceptedFluid();
    	int filled = 0;
        if (resource == null) {
        	filled = 0;
        } else {
        	if (doFill && acceptedFluid == null) {
        		acceptedFluid = resource.getFluid();
        	}
        	if (acceptedFluid == null || acceptedFluid == resource.getFluid()) {
                filled = super.fill(resource, doFill);
            }
        }
        if(filled > 0) {
        	sendUpdate();
        }
        return filled;
    }
    
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        FluidStack drained = super.drain(maxDrain, doDrain);
    	if(drained != null) {
    		sendUpdate();
    	}
    	return drained;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        FluidStack drained = super.drain(resource, doDrain);
        if(drained != null) {
            sendUpdate();
        }
        return drained;
    }

    protected void sendUpdate() {
        if (tile instanceof IUpdateListener) {
            ((IUpdateListener) tile).onTankChanged();
        }
    }

    /**
     * Reset the tank by setting the inner fluid to null.
     */
    public void reset() {
        acceptedFluid = null;
    }

    /**
     * Set the accepted fluid for this tank.
     * @param fluid The accepted fluid
     */
    public void setAcceptedFluid(Fluid fluid) {
        this.acceptedFluid = fluid;
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
            nbt.putString(NBT_ACCEPTED_FLUID, acceptedFluid.getName());
    }

    @Override
    public void readTankFromNBT(CompoundNBT nbt) {
        super.readTankFromNBT(nbt);
        // TODO: enable when Forge finished fluids impl
        //acceptedFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryCreate(nbt.getString(NBT_ACCEPTED_FLUID)));
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        Fluid fluidType = fluid != null ? fluid.getFluid() : null;
        return super.canFillFluidType(fluid) && (fluidType == getAcceptedFluid() || getAcceptedFluid() == null);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
        Fluid fluidType = fluid != null ? fluid.getFluid() : null;
        return super.canDrainFluidType(fluid) && (fluidType == getAcceptedFluid() || getAcceptedFluid() == null);
    }

    /**
     * Implement this on a tile entity to indicate that it should listen to tank changes.
     */
    public static interface IUpdateListener {
        /**
         * Called when the contents of the tank have changed.
         */
        public void onTankChanged();
    }
    
}
