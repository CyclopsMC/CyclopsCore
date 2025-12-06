package org.cyclops.cyclopscore.item;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;

import java.util.Locale;
import java.util.function.Consumer;

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
    public DamageIndicatedItemFluidContainer item;

    /**
     * Create a new DamageIndicatedItemComponent
     *
     * @param item
     *          The item class on which the behaviour will be added.
     */
    public DamageIndicatedItemComponent(DamageIndicatedItemFluidContainer item) {
        this.item = item;
    }

    /**
     * Add the creative tab items.
     * @param items The item list to add to.
     * @param fluid The fluid in the container that needs to be added.
     */
    public void fillDefaultCreativeTabEntries(NonNullList<ItemStack> items, Fluid fluid) {
        // Add the 'full' container.
        ItemAccess itemStackFullAccess = ItemAccess.forStack(new ItemStack(this.item));
        IFluidHandlerCapacity fluidHanderFull = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(itemStackFullAccess).orElse(null);
        if (fluidHanderFull != null) {
            try (var tx = Transaction.openRoot()) {
                fluidHanderFull.insert(FluidResource.of(fluid), fluidHanderFull.getTankCapacity(0), tx);
                tx.commit();
            }
        }
        items.add(itemStackFullAccess.getResource().toStack());

        // Add the 'empty' container.
        ItemStack itemStackEmpty = new ItemStack(item);
        items.add(itemStackEmpty);
    }

    /**
     * Get hovering info for the given {@link ItemStack}.
     * @param itemStack The item stack to add the info for.
     * @return The info for the item.
     */
    public MutableComponent getInfo(ItemStack itemStack) {
        int amount = 0;
        FluidStack fluidStack = net.neoforged.neoforge.transfer.fluid.FluidUtil.getFirstStackContained(itemStack);
        if (!fluidStack.isEmpty())
            amount = fluidStack.getAmount();
        IFluidHandlerCapacity fluidHander = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(itemStack)).orElse(null);
        return getInfo(fluidStack, amount, fluidHander == null ? 0 : fluidHander.getTankCapacity(0));
    }

    /**
     * Get hovering info for the given amount and capacity.
     * @param fluidStack The fluid stack for this container, can be null.
     * @param amount The amount to show.
     * @param capacity The capacity to show.
     * @return The info generated from the given parameters.
     */
    public static MutableComponent getInfo(FluidStack fluidStack, int amount, int capacity) {
        MutableComponent prefix = Component.literal("");
        if (!fluidStack.isEmpty()) {
            prefix = fluidStack.getHoverName().copy().append(": ");
        }
        return prefix
                .append(String.format(Locale.ROOT, "%,d", amount))
                .append(" / ")
                .append(String.format(Locale.ROOT, "%,d", capacity))
                .append(" mB");
    }

    /**
     * Add information to the given list for the given item.
     *
     * @param itemStack      The {@link ItemStack} to add info for.
     * @param context        The context that will see the info.
     * @param tooltipDisplay
     * @param tooltipAdder   The info list where the info will be added.
     * @param flag           the tooltip flag
     */
    public void addInformation(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(((IInformationProvider) itemStack.getItem()).getInfo(itemStack)
                .setStyle(Style.EMPTY.withColor(IInformationProvider.ITEM_PREFIX)));
    }

    /**
     * Get the displayed durability value for the given {@link ItemStack}.
     * @param itemStack The {@link ItemStack} to get the displayed damage for.
     * @return The displayed durability.
     */
    public int getDurability(ItemStack itemStack) {
        FluidStack fluidStack = net.neoforged.neoforge.transfer.fluid.FluidUtil.getFirstStackContained(itemStack);
        IFluidHandlerCapacity fluidHander = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(itemStack)).orElse(null);
        double capacity = fluidHander == null ? 0 : fluidHander.getTankCapacity(0);
        double amount = IModHelpersNeoForge.get().getFluidHelpers().getAmount(fluidStack);
        return (int) Math.round(amount * 13 / capacity);
    }

}
