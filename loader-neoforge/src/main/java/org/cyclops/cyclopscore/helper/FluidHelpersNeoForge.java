package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.Capabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author rubensworks
 */
public class FluidHelpersNeoForge implements IFluidHelpersNeoForge {
    @Override
    public int getBucketVolume() {
        return 1000;
    }

    @Override
    public int getAmount(FluidStack fluidStack) {
        return fluidStack.getAmount();
    }

    @Override
    public FluidStack copy(FluidStack fluidStack) {
        if(fluidStack.isEmpty()) return FluidStack.EMPTY;
        return fluidStack.copy();
    }

    @Override
    public boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination) {
        FluidStack drained = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        return !drained.isEmpty() && destination.fill(drained, IFluidHandler.FluidAction.SIMULATE) == drained.getAmount();
    }

    @Override
    public FluidStack getFluid(@Nullable IFluidHandler fluidHandler) {
        return fluidHandler != null ? fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE) : FluidStack.EMPTY;
    }

    @Override
    public boolean hasFluid(@Nullable IFluidHandler fluidHandler) {
        return !getFluid(fluidHandler).isEmpty();
    }

    @Override
    public int getCapacity(@Nullable IFluidHandler fluidHandler) {
        int capacity = 0;
        if (fluidHandler != null) {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                capacity += fluidHandler.getTankCapacity(i);
            }
        }
        return capacity;
    }

    @Override
    public Optional<IFluidHandlerItemCapacity> getFluidHandlerItemCapacity(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.getCapability(Capabilities.Item.FLUID_HANDLER_CAPACITY));
    }

    @Override
    public FluidStack extractFromInventory(int amount, @Nullable ItemStack blacklistedStack, @Nullable Fluid fluidWhitelist, Player player, IFluidHandler.FluidAction action) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        Wrapper<FluidStack> drained = new Wrapper<>(FluidStack.EMPTY);
        Wrapper<Integer> amountHolder = new Wrapper<>(amount);
        while (it.hasNext() && amountHolder.get() > 0) {
            ItemStack current = it.next();
            if (current != null && current != blacklistedStack && FluidUtil.getFluidHandler(current) != null) {
                FluidUtil.getFluidHandler(current).ifPresent(fluidHandler -> {
                    FluidStack totalFluid = getFluid(fluidHandler);
                    if (!totalFluid.isEmpty() && (fluidWhitelist == null || totalFluid.getFluid() == fluidWhitelist)) {
                        FluidStack thisDrained = fluidHandler.drain(amountHolder.get(), action);
                        if (!thisDrained.isEmpty() && (fluidWhitelist == null || thisDrained.getFluid() == fluidWhitelist)) {
                            if (drained.get().isEmpty()) {
                                drained.set(thisDrained);
                            } else {
                                drained.get().setAmount(drained.get().getAmount() + thisDrained.getAmount());
                            }
                            amountHolder.set(amountHolder.get() - thisDrained.getAmount());
                        }
                    }
                });
            }
        }
        if(drained.get() != null && drained.get().getAmount() == 0) {
            drained.set(FluidStack.EMPTY);
        }
        return drained.get();
    }

    @Override
    public FluidStack extractFromItemOrInventory(int amount, ItemStack itemStack, @Nullable Player player, IFluidHandler.FluidAction action) {
        if (action.execute() && player != null && player.isCreative() && !player.level().isClientSide()) {
            action = IFluidHandler.FluidAction.SIMULATE;
        }
        if (amount == 0) return FluidStack.EMPTY;
        IFluidHandler.FluidAction finalAction = action;
        return FluidUtil.getFluidHandler(itemStack).map((fluidHandler) -> {
            FluidStack drained = fluidHandler.drain(amount, finalAction);
            if (!drained.isEmpty() && drained.getAmount() == amount) return drained;
            int drainedAmount = (drained.isEmpty() ? 0 : drained.getAmount());
            int toDrain = amount - drainedAmount;
            FluidStack otherDrained = player == null ? null : extractFromInventory(toDrain, itemStack,
                    getFluid(fluidHandler).getFluid(), player, finalAction);
            if (otherDrained == null) return drained;
            otherDrained.setAmount(otherDrained.getAmount() + drainedAmount);
            return otherDrained;
        }).orElse(FluidStack.EMPTY);
    }

    @Override
    public void placeOrPickUpFluid(Player player, InteractionHand hand, Level world, BlockPos blockPos, Direction side) {
        ItemStack itemStack = player.getItemInHand(hand);
        ItemStack itemStackResult = FluidHelpers.getFluidHandlerItemCapacity(itemStack).map(fluidHandler -> {
            FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(FluidStack.EMPTY);
            FluidStack drained = fluidHandler.drain(FluidHelpers.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);

            // Try picking up a fluid if we have space in the container
            if (fluidStack.isEmpty() || (fluidStack.getAmount() + FluidHelpers.BUCKET_VOLUME <= fluidHandler.getCapacity())) {
                FluidActionResult resultPickUp = FluidUtil.tryPickUpFluid(itemStack, player, world, blockPos, side);
                if (resultPickUp.isSuccess()) {
                    return resultPickUp.getResult();
                }
            }

            // Try placing a fluid if we have something container
            if (!drained.isEmpty() && (drained.getAmount() > 0)) {
                FluidActionResult resultPlace = FluidUtil.tryPlaceFluid(player, world, hand, blockPos, itemStack, fluidStack);
                if (resultPlace.isSuccess()) {
                    return resultPlace.getResult();
                }
            }

            return itemStack;
        }).orElse(itemStack);
        player.setItemInHand(hand, itemStackResult);
    }

    @Override
    public IFluidHandler.FluidAction simulateBooleanToAction(boolean simulate) {
        return simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }
}
