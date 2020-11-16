package org.cyclops.cyclopscore.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;

import java.util.List;
import java.util.function.Supplier;

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
    protected Supplier<Fluid> fluid;
    
    /**
     * Create a new DamageIndicatedItemFluidContainer.
     *
     * @param builder Item properties builder.
     * @param capacity The capacity this container will have.
     * @param fluid The Fluid instance this container must hold.
     */
    public DamageIndicatedItemFluidContainer(Item.Properties builder, int capacity, Supplier<Fluid> fluid) {
        super(builder, capacity);
        this.fluid = fluid;
        init();
    }
    
    private void init() {
        component = new DamageIndicatedItemComponent(this);
    }

    @Override
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            component.fillItemGroup(itemGroup, items, fluid.get());
        }
    }

    @Override
    public ITextComponent getInfo(ItemStack itemStack) {
        return component.getInfo(itemStack);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        // Can be null during startup
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            component.addInformation(itemStack, world, list, flag);
        }
        super.addInformation(itemStack, world, list, flag);
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
        return this.fluid.get();
    }
    
    /**
     * If the given amount can be drained. (Will drain in simulation mode)
     * @param amount The amount to try to drain.
     * @param itemStack The item stack to drain from.
     * @return If it could be drained.
     */
    public boolean canDrain(int amount, ItemStack itemStack) {
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
        if (fluidHandler == null) return false;
    	FluidStack simulatedDrain = fluidHandler.drain(amount, IFluidHandler.FluidAction.SIMULATE);
    	return simulatedDrain != null && simulatedDrain.getAmount() == amount;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new FluidHandlerItemCapacity(stack, capacity, getFluid());
    }
}
