package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.Capabilities;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.datastructure.Wrapper;
import org.cyclops.cyclopscore.inventory.ItemAccessItemLocation;
import org.cyclops.cyclopscore.inventory.ItemLocation;
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
    public FluidStack getFluid(@Nullable ResourceHandler<FluidResource> fluidHandler) {
        return fluidHandler != null ? FluidUtil.getStack(fluidHandler, 0) : FluidStack.EMPTY;
    }

    @Override
    public boolean hasFluid(@Nullable ResourceHandler<FluidResource> fluidHandler) {
        return !getFluid(fluidHandler).isEmpty();
    }

    @Override
    public long getCapacity(@Nullable ResourceHandler<FluidResource> fluidHandler) {
        long capacity = 0;
        if (fluidHandler != null) {
            for (int i = 0; i < fluidHandler.size(); i++) {
                capacity += fluidHandler.getCapacityAsLong(i, fluidHandler.getResource(i));
            }
        }
        return capacity;
    }

    @Override
    public Optional<IFluidHandlerCapacity> getFluidHandlerItemCapacity(ItemAccess itemAccess) {
        return Optional.ofNullable(itemAccess.getCapability(Capabilities.Item.FLUID_HANDLER_CAPACITY));
    }

    @Override
    public FluidStack extractFromInventory(int amount, @Nullable ItemStack blacklistedStack, @Nullable Fluid fluidWhitelist, Player player, Transaction transaction) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        Wrapper<FluidStack> drained = new Wrapper<>(FluidStack.EMPTY);
        Wrapper<Integer> amountHolder = new Wrapper<>(amount);
        while (it.hasNext() && amountHolder.get() > 0) {
            ItemLocation currentLocation = it.nextIndexed();
            ItemAccess current = new ItemAccessItemLocation(player, currentLocation);
            ResourceHandler<FluidResource> fluidHandler = current.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Fluid.ITEM);
            if (currentLocation.getItemStack(player) != blacklistedStack && fluidHandler != null) {
                FluidStack totalFluid = getFluid(fluidHandler);
                if (!totalFluid.isEmpty() && (fluidWhitelist == null || totalFluid.getFluid() == fluidWhitelist)) {
                    int thisDrained = fluidHandler.extract(FluidResource.of(totalFluid), amountHolder.get(), transaction);
                    if (thisDrained > 0) {
                        if (drained.get().isEmpty()) {
                            drained.set(totalFluid.copyWithAmount(thisDrained));
                        } else {
                            drained.get().setAmount(drained.get().getAmount() + thisDrained);
                        }
                        amountHolder.set(amountHolder.get() - thisDrained);
                    }
                }
            }
        }
        if(drained.get() != null && drained.get().getAmount() == 0) {
            drained.set(FluidStack.EMPTY);
        }
        return drained.get();
    }

    @Override
    public FluidStack extractFromItemOrInventory(int amount, ItemStack itemStack, @Nullable Player player, Transaction transaction) {
        if (amount == 0) return FluidStack.EMPTY;
        ItemAccess itemAccess = ItemAccess.forStack(itemStack);
        ResourceHandler<FluidResource> fluidHandler = itemAccess.getCapability(net.neoforged.neoforge.capabilities.Capabilities.Fluid.ITEM);
        if (fluidHandler == null) return FluidStack.EMPTY;
        FluidResource fluidResource = FluidResource.of(getFluid(fluidHandler));
        int drained = fluidHandler.extract(fluidResource, amount, transaction);
        if (drained == amount) return fluidResource.toStack(drained);
        int toDrain = amount - drained;
        FluidStack otherDrained = player == null ? null : extractFromInventory(toDrain, itemStack,
                getFluid(fluidHandler).getFluid(), player, transaction);
        if (otherDrained == null) return fluidResource.toStack(drained);
        otherDrained.setAmount(otherDrained.getAmount() + drained);
        return otherDrained;
    }

    @Override
    public FluidStack placeOrPickUpFluid(Player player, InteractionHand hand, Level world, BlockPos blockPos, Direction side) {
        ItemAccess itemAccess = ItemAccess.forPlayerInteraction(player, hand);
        return getFluidHandlerItemCapacity(itemAccess).map(fluidHandler -> {
            FluidStack fluidStack = FluidUtil.getFirstStackContained(itemAccess.getResource().toStack());

            // Try picking up a fluid if we have space in the container
            if (fluidStack.isEmpty() || (fluidStack.getAmount() + IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume() <= fluidHandler.getTankCapacity(0))) {
                FluidStack resultPickUp = FluidUtil.tryPickupFluid(fluidHandler, player, world, blockPos, side);
                if (!resultPickUp.isEmpty()) {
                    return resultPickUp;
                }
            }

            // Try placing a fluid if we have something container
            int drained;
            try (var tx = Transaction.openRoot()) {
                drained = fluidHandler.extract(FluidResource.of(fluidStack), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), tx);
            }
            if (drained > 0) {
                FluidStack resultPlace = FluidUtil.tryPlaceFluid(fluidHandler, player, world, hand, blockPos);
                if (!resultPlace.isEmpty()) {
                    return resultPlace;
                }
            }

            return FluidStack.EMPTY;
        }).orElse(FluidStack.EMPTY);
    }
}
