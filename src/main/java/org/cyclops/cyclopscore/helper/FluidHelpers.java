package org.cyclops.cyclopscore.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacityConfig;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import javax.annotation.Nullable;

/**
 * Contains helper methods for various fluid specific things.
 * @author rubensworks
 */
public final class FluidHelpers {

    /**
     * Get the fluid amount of the given stack in a safe manner.
     * @param fluidStack The fluid stack
     * @return The fluid amount.
     */
    public static int getAmount(@Nullable FluidStack fluidStack) {
        return fluidStack != null ? fluidStack.amount : 0;
    }

    /**
     * Copy the given fluid stack
     * @param fluidStack The fluid stack to copy.
     * @return A copy of the fluid stack.
     */
    public static FluidStack copy(@Nullable FluidStack fluidStack) {
        if(fluidStack == null) return null;
        return fluidStack.copy();
    }

    /**
     * If this destination can completely contain the given fluid in the given source.
     * @param source The source of the fluid that has to be moved.
     * @param destination The target of the fluid that has to be moved.
     * @return If the destination can completely contain the fluid of the source.
     */
    public static boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination) {
        FluidStack drained = source.drain(Integer.MAX_VALUE, false);
        return drained != null && destination.fill(drained, false) == drained.amount;
    }

    /**
     * Get the fluid contained in a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The fluid.
     */
    public static FluidStack getFluid(@Nullable IFluidHandler fluidHandler) {
        return fluidHandler != null ? fluidHandler.drain(Integer.MAX_VALUE, false) : null;
    }

    /**
     * Check if the fluid handler is not empty.
     * @param fluidHandler The fluid handler.
     * @return If it is not empty.
     */
    public static boolean hasFluid(@Nullable IFluidHandler fluidHandler) {
        return getFluid(fluidHandler) != null;
    }

    /**
     * Get the capacity of a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The capacity.
     */
    public static int getCapacity(@Nullable IFluidHandler fluidHandler) {
        if (fluidHandler != null) {
            for (IFluidTankProperties properties : fluidHandler.getTankProperties()) {
                return properties.getCapacity();
            }
        }
        return 0;
    }

    /**
     * @param itemStack The itemstack
     * @return The item capacity fluid handler.
     */
    public static LazyOptional<IFluidHandlerItemCapacity> getFluidHandlerItemCapacity(ItemStack itemStack) {
        return itemStack.getCapability(FluidHandlerItemCapacityConfig.CAPABILITY, null);
    }

    /**
     * Extract the given fluid amount from any item inside the player's inventory.
     * @param amount A fluid amount to extract.
     * @param blacklistedStack The itemstack to skip. Useful if this is the stack that you are inserting to.
     * @param fluidWhitelist A fluid to transfer, can be null to allow any fluid to be transferred.
     * @param player The player to scan the inventory from.
     * @param simulate If extraction should be done.
     * @return The extracted fluidstack.
     */
    @Nullable
    public static FluidStack extractFromInventory(int amount, @Nullable ItemStack blacklistedStack,
                                                  @Nullable Fluid fluidWhitelist, PlayerEntity player,
                                                  boolean simulate) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        Wrapper<FluidStack> drained = new Wrapper<>(null);
        Wrapper<Integer> amountHolder = new Wrapper<>(amount);
        while(it.hasNext() && amountHolder.get() > 0) {
            ItemStack current = it.next();
            if(current != null && current != blacklistedStack && FluidUtil.getFluidHandler(current) != null) {
                FluidUtil.getFluidHandler(current).ifPresent(fluidHandler -> {
                    FluidStack totalFluid = getFluid(fluidHandler);
                    if(totalFluid != null && (fluidWhitelist == null || totalFluid.getFluid() == fluidWhitelist)) {
                        FluidStack thisDrained = fluidHandler.drain(amountHolder.get(), !simulate);
                        if (thisDrained != null && (fluidWhitelist == null || thisDrained.getFluid() == fluidWhitelist)) {
                            if (drained.get() == null) {
                                drained.set(thisDrained);
                            } else {
                                drained.get().amount += thisDrained.amount;
                            }
                            amountHolder.set(amountHolder.get() - thisDrained.amount);
                        }
                    }
                });
            }
        }
        if(drained.get() != null && drained.get().amount == 0) {
            drained.set(null);
        }
        return drained.get();
    }

    /**
     * Extract the given fluid amount from the given item, or from the player's inventory if that fails.
     * @param amount A fluid amount to extract.
     * @param itemStack The item to extract from first.
     * @param player The player to scan the inventory from.
     * @param simulate If extraction should be done.
     * @return The extracted fluidstack.
     */
    @Nullable
    public static FluidStack extractFromItemOrInventory(int amount, ItemStack itemStack,
                                                        @Nullable PlayerEntity player, boolean simulate) {
        if (!simulate && player != null && player.isCreative() && !player.world.isRemote()) {
            simulate = true;
        }
        if (amount == 0) return null;
        boolean finalSimulate = simulate;
        return FluidUtil.getFluidHandler(itemStack).map((fluidHandler) -> {
            FluidStack drained = fluidHandler.drain(amount, !finalSimulate);
            if (drained != null && drained.amount == amount) return drained;
            int drainedAmount = (drained == null ? 0 : drained.amount);
            int toDrain = amount - drainedAmount;
            FluidStack otherDrained = player == null ? null : extractFromInventory(toDrain, itemStack,
                    getFluid(fluidHandler).getFluid(), player, !finalSimulate);
            if (otherDrained == null) return drained;
            otherDrained.amount += drainedAmount;
            return otherDrained;
        }).orElse(null);
    }

    /**
     * Try placing or picking up fluids from the held item.
     * This can be called in {@link Item#onItemRightClick}.
     * @param player The active player.
     * @param hand The active hand.
     * @param world The world.
     * @param blockPos The target position.
     * @param side The target side.
     */
    public static void placeOrPickUpFluid(PlayerEntity player, Hand hand, World world, BlockPos blockPos, Direction side) {
        ItemStack itemStack = player.getHeldItem(hand);
        ItemStack itemStackResult = FluidHelpers.getFluidHandlerItemCapacity(itemStack).map(fluidHandler -> {
            FluidStack fluidStack = FluidUtil.getFluidContained(itemStack).orElse(null);
            FluidStack drained = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);

            // Try picking up a fluid if we have space in the container
            if (fluidStack == null || (fluidStack.amount + Fluid.BUCKET_VOLUME <= fluidHandler.getCapacity())) {
                FluidActionResult resultPickUp = FluidUtil.tryPickUpFluid(itemStack, player, world, blockPos, side);
                if (resultPickUp.isSuccess()) {
                    return resultPickUp.getResult();
                }
            }

            // Try placing a fluid if we have something container
            if (drained != null && (drained.amount > 0)) {
                FluidActionResult resultPlace = FluidUtil.tryPlaceFluid(player, world, hand, blockPos, itemStack, fluidStack);
                if (resultPlace.isSuccess()) {
                    return resultPlace.getResult();
                }
            }

            return itemStack;
        }).orElse(itemStack);
        player.setHeldItem(hand, itemStackResult);
    }

}
