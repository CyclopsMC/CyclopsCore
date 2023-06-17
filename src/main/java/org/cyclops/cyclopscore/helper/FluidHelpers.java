package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.Capabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;

/**
 * Contains helper methods for various fluid specific things.
 * @author rubensworks
 */
public final class FluidHelpers {

    public static final int BUCKET_VOLUME = 1000;

    /**
     * Get the fluid amount of the given stack in a safe manner.
     * @param fluidStack The fluid stack
     * @return The fluid amount.
     */
    public static int getAmount(FluidStack fluidStack) {
        return fluidStack.getAmount();
    }

    /**
     * Copy the given fluid stack
     * @param fluidStack The fluid stack to copy.
     * @return A copy of the fluid stack.
     */
    public static FluidStack copy(FluidStack fluidStack) {
        if(fluidStack.isEmpty()) return FluidStack.EMPTY;
        return fluidStack.copy();
    }

    /**
     * If this destination can completely contain the given fluid in the given source.
     * @param source The source of the fluid that has to be moved.
     * @param destination The target of the fluid that has to be moved.
     * @return If the destination can completely contain the fluid of the source.
     */
    public static boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination) {
        FluidStack drained = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        return !drained.isEmpty() && destination.fill(drained, IFluidHandler.FluidAction.SIMULATE) == drained.getAmount();
    }

    /**
     * Get the fluid contained in a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The fluid.
     */
    public static FluidStack getFluid(@Nullable IFluidHandler fluidHandler) {
        return fluidHandler != null ? fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE) : FluidStack.EMPTY;
    }

    /**
     * Check if the fluid handler is not empty.
     * @param fluidHandler The fluid handler.
     * @return If it is not empty.
     */
    public static boolean hasFluid(@Nullable IFluidHandler fluidHandler) {
        return !getFluid(fluidHandler).isEmpty();
    }

    /**
     * Get the capacity of a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The capacity.
     */
    public static int getCapacity(@Nullable IFluidHandler fluidHandler) {
        int capacity = 0;
        if (fluidHandler != null) {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                capacity += fluidHandler.getTankCapacity(i);
            }
        }
        return capacity;
    }

    /**
     * @param itemStack The itemstack
     * @return The item capacity fluid handler.
     */
    public static LazyOptional<IFluidHandlerItemCapacity> getFluidHandlerItemCapacity(ItemStack itemStack) {
        return itemStack.getCapability(Capabilities.FLUID_HANDLER_ITEM_CAPACITY);
    }

    /**
     * Extract the given fluid amount from any item inside the player's inventory.
     * @param amount A fluid amount to extract.
     * @param blacklistedStack The itemstack to skip. Useful if this is the stack that you are inserting to.
     * @param fluidWhitelist A fluid to transfer, can be null to allow any fluid to be transferred.
     * @param player The player to scan the inventory from.
     * @param action The fluid action.
     * @return The extracted fluidstack.
     */
    public static FluidStack extractFromInventory(int amount, @Nullable ItemStack blacklistedStack,
                                                  @Nullable Fluid fluidWhitelist, Player player,
                                                  IFluidHandler.FluidAction action) {
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

    /**
     * Extract the given fluid amount from the given item, or from the player's inventory if that fails.
     * @param amount A fluid amount to extract.
     * @param itemStack The item to extract from first.
     * @param player The player to scan the inventory from.
     * @param action The fluid action.
     * @return The extracted fluidstack.
     */
    public static FluidStack extractFromItemOrInventory(int amount, ItemStack itemStack,
                                                        @Nullable Player player,
                                                        IFluidHandler.FluidAction action) {
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

    /**
     * Try placing or picking up fluids from the held item.
     * This can be called in {@link Item#use}.
     * @param player The active player.
     * @param hand The active hand.
     * @param world The world.
     * @param blockPos The target position.
     * @param side The target side.
     */
    public static void placeOrPickUpFluid(Player player, InteractionHand hand, Level world, BlockPos blockPos, Direction side) {
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

    /**
     * Convert a boolean-based simulate to a fluid action enum value.
     * @param simulate If in simulation mode.
     * @return The fluid action.
     */
    public static IFluidHandler.FluidAction simulateBooleanToAction(boolean simulate) {
        return simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }

}
