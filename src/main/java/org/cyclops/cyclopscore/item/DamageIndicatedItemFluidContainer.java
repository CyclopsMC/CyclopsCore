package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

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
    public void fillItemCategory(CreativeModeTab itemGroup, NonNullList<ItemStack> items) {
        if (!ItemStackHelpers.isValidCreativeTab(this, itemGroup)) return;
        if (this.allowdedIn(category)) {
            component.fillItemGroup(itemGroup, items, fluid.get());
        }
    }

    @Override
    public MutableComponent getInfo(ItemStack itemStack) {
        return component.getInfo(itemStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        // Can be null during startup
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            component.addInformation(itemStack, world, list, flag);
        }
        super.appendHoverText(itemStack, world, list, flag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return component.getDurability(itemStack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(Math.max(0.0F, 1 - (float) component.getDurability(stack)) / 3.0F, 1.0F, 1.0F);
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
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandlerItemCapacity(stack, capacity, getFluid());
    }
}
