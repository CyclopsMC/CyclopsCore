package org.cyclops.cyclopscore.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;

import java.util.List;
import java.util.Locale;

/**
 * A component that has to be added for classes that want to implement the DamageIndicator behaviour.
 * 
 * Items can add this component (Composite design-pattern) to any item that needs to have a damage
 * indicator based on a custom value. Like for example the amount of energy left in an IC2 electrical
 * wrench, or the amount of MJ's left in a redstone energy cell from Thermal Expansion.
 * 
 * See {@link DamageIndicatedItemFluidContainer} for an example.
 * This could be for example an Item or an ItemFluidContainer.
 * 
 * @author rubensworks
 *
 */
public class DamageIndicatedItemComponent {
    
    /**
     * The item class on which the behaviour will be added.
     */
    public ItemFluidContainer item;

    /**
     * Create a new DamageIndicatedItemComponent
     * 
     * @param item
     *          The item class on which the behaviour will be added.
     */
    public DamageIndicatedItemComponent(ItemFluidContainer item) {
        this.item = item;
    }
    
    /**
     * Add the creative tab items.
     * @param itemGroup The item group.
     * @param items The item list to add to.
     * @param fluid The fluid in the container that needs to be added.
     */
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> items, Fluid fluid) {
        // Add the 'full' container.
        ItemStack itemStackFull = new ItemStack(this.item);
        IFluidHandlerItemCapacity fluidHanderFull = FluidHelpers.getFluidHandlerItemCapacity(itemStackFull).orElse(null);
        if (fluidHanderFull != null) {
            fluidHanderFull.fill(new FluidStack(fluid, fluidHanderFull.getCapacity()), IFluidHandler.FluidAction.EXECUTE);
        }
        items.add(itemStackFull);
        
        // Add the 'empty' container.
        ItemStack itemStackEmpty = new ItemStack(item);
        items.add(itemStackEmpty);
    }
    
    /**
     * Get hovering info for the given {@link ItemStack}.
     * @param itemStack The item stack to add the info for.
     * @return The info for the item.
     */
    public IFormattableTextComponent getInfo(ItemStack itemStack) {
        int amount = 0;
        IFluidHandlerItemCapacity fluidHander = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        if (!fluidStack.isEmpty())
            amount = fluidStack.getAmount();
        return getInfo(fluidStack, amount, fluidHander == null ? 0 : fluidHander.getCapacity());
    }
    
    /**
     * Get hovering info for the given amount and capacity.
     * @param fluidStack The fluid stack for this container, can be null.
     * @param amount The amount to show.
     * @param capacity The capacity to show.
     * @return The info generated from the given parameters.
     */
    @OnlyIn(Dist.CLIENT)
    public static IFormattableTextComponent getInfo(FluidStack fluidStack, int amount, int capacity) {
        IFormattableTextComponent prefix = new StringTextComponent("");
    	if (!fluidStack.isEmpty()) {
    		prefix = new TranslationTextComponent(fluidStack.getTranslationKey()).append(": ");
    	}
        return prefix
                .append(String.format(Locale.ROOT, "%,d", amount))
                .append(" / ")
                .append(String.format(Locale.ROOT, "%,d", capacity))
                .append(" mB");
    }
    
    /**
     * Add information to the given list for the given item.
     * @param itemStack The {@link ItemStack} to add info for.
     * @param world The player that will see the info.
     * @param list The info list where the info will be added.
     * @param flag the tooltip flag
     */
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(((IInformationProvider) itemStack.getItem()).getInfo(itemStack)
                .setStyle(Style.EMPTY.withColor(IInformationProvider.ITEM_PREFIX)));
    }
    
    /**
     * Get the displayed durability value for the given {@link ItemStack}.
     * @param itemStack The {@link ItemStack} to get the displayed damage for.
     * @return The displayed durability.
     */
    public double getDurability(ItemStack itemStack) {
        IFluidHandlerItemCapacity fluidHander = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
        double capacity = fluidHander == null ? 0 : fluidHander.getCapacity();
        double amount = FluidHelpers.getAmount(fluidStack);
        return (capacity - amount) / capacity;
    }
    
}
