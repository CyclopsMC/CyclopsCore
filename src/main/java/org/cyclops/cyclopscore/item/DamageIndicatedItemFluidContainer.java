package org.cyclops.cyclopscore.item;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * This extension on {@link Item} with a fluid capability will show a damage indicator depending on how full
 * the container is. This can be used to hold certain amounts of Fluids in an Item.
 * When this item is available in a CreativeTab, it will add itself as a full and an empty container.
 *
 * This container ONLY allows the fluid from the given type.
 *
 * @author rubensworks
 *
 */
public abstract class DamageIndicatedItemFluidContainer extends Item implements IInformationProvider {

    protected final int capacity;
    protected DamageIndicatedItemComponent component;
    protected final Supplier<Fluid> fluid;

    /**
     * Create a new DamageIndicatedItemFluidContainer.
     *
     * @param builder Item properties builder.
     * @param capacity The capacity this container will have.
     * @param fluid The Fluid instance this container must hold.
     */
    public DamageIndicatedItemFluidContainer(Item.Properties builder, int capacity, Supplier<Fluid> fluid) {
        super(builder);
        this.capacity = capacity;
        this.fluid = fluid;
        init();

        CyclopsCore._instance.getModEventBus().addListener(this::registerCapability);
    }

    private void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new FluidHandlerItemCapacity(itemStack, capacity, getFluid()), this);
        event.registerItem(org.cyclops.cyclopscore.Capabilities.Item.FLUID_HANDLER_CAPACITY, (itemStack, context) -> new FluidHandlerItemCapacity(itemStack, capacity, getFluid()), this);
    }

    private void init() {
        component = new DamageIndicatedItemComponent(this);
    }

    public Collection<ItemStack> getDefaultCreativeTabEntries() {
        NonNullList<ItemStack> list = NonNullList.create();
        component.fillDefaultCreativeTabEntries(list, fluid.get());
        return list;
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
        component.addInformation(itemStack, world, list, flag);
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
        return Mth.hsvToRgb(Math.max(0.0F, ((float) getBarWidth(stack) / 13)) / 3.0F, 1.0F, 1.0F);
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
}
