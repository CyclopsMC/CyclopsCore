package org.cyclops.cyclopscore.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;

import java.util.List;

/**
 * This extension on {@link ItemFluidContainer} with a fluid capability will show a damage indicator depending on how full
 * the container is. This can be used to hold certain amounts of Fluids in an Item.
 * When this item is available in a CreativeTab, it will add itself as a full and an empty container.
 * 
 * This container ONLY allows the fluid from the given type.
 * 
 * @author rubensworks
 *
 */
public abstract class DamageIndicatedItemFluidContainer extends ItemFluidContainer implements IInformationProvider {

    protected DamageIndicatedItemComponent component;
    protected Fluid fluid;
    
    /**
     * Create a new DamageIndicatedItemFluidContainer.
     * 
     * @param capacity
     *          The capacity this container will have.
     * @param fluid
     *          The Fluid instance this container must hold.
     */
    public DamageIndicatedItemFluidContainer(int capacity, Fluid fluid) {
        super(capacity);
        this.fluid = fluid;
        init();
    }
    
    private void init() {
        component = new DamageIndicatedItemComponent(this);
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> itemList) {
        component.getSubItems(item, tab, itemList, fluid, 0);
    }

    @Override
    public String getInfo(ItemStack itemStack) {
        return component.getInfo(itemStack);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        
    }
    
    @SuppressWarnings("rawtypes")
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        component.addInformation(itemStack, entityPlayer, list, par4);
        super.addInformation(itemStack, entityPlayer, list, par4);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
    	return true;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
    	return component.getDurability(itemStack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, 1 - (float) component.getDurability(stack)) / 3.0F, 1.0F, 1.0F);
    }
    
    /**
     * Get the fluid.
     * @return The fluid.
     */
    public Fluid getFluid() {
        return this.fluid;
    }
    
    /**
     * If the given amount can be drained. (Will drain in simulation mode)
     * @param amount The amount to try to drain.
     * @param itemStack The item stack to drain from.
     * @return If it could be drained.
     */
    public boolean canDrain(int amount, ItemStack itemStack) {
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack);
        if (fluidHandler == null) return false;
    	FluidStack simulatedDrain = fluidHandler.drain(amount, false);
    	return simulatedDrain != null && simulatedDrain.amount == amount;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemCapacity(stack, capacity);
    }
}
