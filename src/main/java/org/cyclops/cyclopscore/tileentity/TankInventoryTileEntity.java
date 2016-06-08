package org.cyclops.cyclopscore.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.fluid.SingleUseTank;

/**
 * A TileEntity that has an inventory and a tank that can accept fluids or only one type of fluid.
 * @author rubensworks
 *
 */
public abstract class TankInventoryTileEntity extends InventoryTileEntity {
    
    private SingleUseTank tank;
    protected int tankSize;
    private String tankName;
    protected boolean sendUpdateOnTankChanged = false;
    protected final EnumFacingMap<IFluidHandler> sidedFluidHandlers;

    /**
     * Make new tile with a tank that can accept anything and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param stackSize The maximum stacksize each slot can have.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int tankSize, String tankName, int stackSize) {
        super(inventorySize, inventoryName, stackSize);
        this.tankSize = tankSize;
        this.tankName = tankName;
        this.setSendUpdateOnTankChanged(true);
        tank = newTank(tankName, tankSize);

        sidedFluidHandlers = EnumFacingMap.newMap();
        for(EnumFacing side : EnumFacing.VALUES) {
            addCapabilitySided(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side, tank);
        }
    }

    /**
     * Make new tile with a tank that can accept anything and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int tankSize, String tankName) {
        this(inventorySize, inventoryName, tankSize, tankName, 64);
    }
    
    protected SingleUseTank newTank(String tankName, int tankSize) {
        return new SingleUseTank(tankName, tankSize, this);
    }
    
    /**
     * Make new tile with a tank that can accept anything and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param stackSize The maximum stacksize each slot can have
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int stackSize, int tankSize, String tankName) {
        super(inventorySize, inventoryName, stackSize);
        this.tankSize = tankSize;
        this.tankName = tankName;
        this.setSendUpdateOnTankChanged(true);
        tank = newTank(tankName, tankSize);

        sidedFluidHandlers = EnumFacingMap.newMap();
        for(EnumFacing side : EnumFacing.VALUES) {
            addCapabilitySided(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side, tank);
        }
    }
    
    /**
     * Make new tile with a tank that can accept only one fluid and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int tankSize, String tankName, Fluid acceptedFluid) {
        this(inventorySize, inventoryName, tankSize, tankName);
        this.tankSize = tankSize;
        this.tankName = tankName;
        tank.setAcceptedFluid(acceptedFluid);
    }
    
    /**
     * Make new tile with a tank that can accept only one fluid and an inventory.
     * @param inventorySize Amount of slots in the inventory.
     * @param inventoryName Internal name of the inventory.
     * @param stackSize The maximum stacksize each slot can have
     * @param tankSize Size (mB) of the tank.
     * @param tankName Internal name of the tank.
     * @param acceptedFluid Type of Fluid to accept.
     */
    public TankInventoryTileEntity(int inventorySize, String inventoryName, int stackSize, int tankSize, String tankName, Fluid acceptedFluid) {
        this(inventorySize, inventoryName, stackSize, tankSize, tankName);
        this.tankSize = tankSize;
        this.tankName = tankName;
        tank.setAcceptedFluid(acceptedFluid);
    }
    
    /**
     * Get the internal tank
     * @return The internal SingleUseTank
     */
    public SingleUseTank getTank() {
        return tank;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tank.writeToNBT(tag);
        return tag;
    }
    
    /**
     * Fills fluid into internal tanks.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param doFill If false, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    public int fill(FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    /**
     * Drains fluid out of internal tanks.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param doDrain If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return tank.drain(resource, doDrain);
    }

    /**
     * Drains fluid out of internal tanks.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param doDrain If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    /**
     * If this tile should send blockState updates when the tank has changed.
     * @return If it should send blockState updates.
     */
    public boolean isSendUpdateOnTankChanged() {
        return sendUpdateOnTankChanged;
    }

    /**
     * If this tile should send blockState updates when the tank has changed.
     * @param sendUpdateOnTankChanged If it should send blockState updates.
     */
    public void setSendUpdateOnTankChanged(boolean sendUpdateOnTankChanged) {
        this.sendUpdateOnTankChanged = sendUpdateOnTankChanged;
    }
    
    @Override
    protected void onSendUpdate() {
    	super.onSendUpdate();
    	if(worldObj.getBlockState(getPos()).hasComparatorInputOverride()) {
    		worldObj.notifyNeighborsOfStateChange(getPos(), this.getBlock());
    	}
    }

}
