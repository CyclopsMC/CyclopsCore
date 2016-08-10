package org.cyclops.cyclopscore.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.cyclopscore.tileentity.TankInventoryTileEntity;

/**
 * A simple tank that can accept and drain fluids until the capacity is reached.
 * Only one fluid can be accepted, which must be specified with {@link SingleUseTank#setAcceptedFluid(Fluid)}.
 * Based on the Buildcraft SingleUseTank.
 * @author rubensworks
 *
 */
public class SingleUseTank extends Tank {
    
    /**
     * The NBT name for the fluid tank.
     */
    public static final String NBT_ACCEPTED_FLUID = "acceptedFluid";
    
    private Fluid acceptedFluid;
    protected CyclopsTileEntity tile;

    /**
     * Make a new tank instance.
     * @param name The name for the tank, will be used for NBT storage.
     * @param capacity The capacity (mB) for the tank.
     * @param tile The TileEntity that uses this tank.
     */
    public SingleUseTank(String name, int capacity, CyclopsTileEntity tile) {
        super(name, capacity, tile);
        this.tile = tile;
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
    	// TODO: generalize this to accept ITankUpdateListeners if this would be necessary later.
    	if(!(tile instanceof TankInventoryTileEntity) || ((TankInventoryTileEntity) tile).isSendUpdateOnTankChanged()) {
    		tile.sendUpdate();
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
    public void writeTankToNBT(NBTTagCompound nbt) {
        super.writeTankToNBT(nbt);
        if (acceptedFluid != null)
            nbt.setString(NBT_ACCEPTED_FLUID, acceptedFluid.getName());
    }

    @Override
    public void readTankFromNBT(NBTTagCompound nbt) {
        super.readTankFromNBT(nbt);
        acceptedFluid = FluidRegistry.getFluid(nbt.getString(NBT_ACCEPTED_FLUID));
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
    
}
