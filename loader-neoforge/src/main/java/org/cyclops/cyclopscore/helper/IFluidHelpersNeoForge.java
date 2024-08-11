package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author rubensworks
 */
public interface IFluidHelpersNeoForge {

    public int getBucketVolume();

    /**
     * Get the fluid amount of the given stack in a safe manner.
     * @param fluidStack The fluid stack
     * @return The fluid amount.
     */
    public int getAmount(FluidStack fluidStack);

    /**
     * Copy the given fluid stack
     * @param fluidStack The fluid stack to copy.
     * @return A copy of the fluid stack.
     */
    public FluidStack copy(FluidStack fluidStack);

    /**
     * If this destination can completely contain the given fluid in the given source.
     * @param source The source of the fluid that has to be moved.
     * @param destination The target of the fluid that has to be moved.
     * @return If the destination can completely contain the fluid of the source.
     */
    public boolean canCompletelyFill(IFluidHandler source, IFluidHandler destination);

    /**
     * Get the fluid contained in a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The fluid.
     */
    public FluidStack getFluid(@Nullable IFluidHandler fluidHandler);

    /**
     * Check if the fluid handler is not empty.
     * @param fluidHandler The fluid handler.
     * @return If it is not empty.
     */
    public boolean hasFluid(@Nullable IFluidHandler fluidHandler);

    /**
     * Get the capacity of a fluid handler.
     * @param fluidHandler The fluid handler.
     * @return The capacity.
     */
    public int getCapacity(@Nullable IFluidHandler fluidHandler);

    /**
     * @param itemStack The itemstack
     * @return The item capacity fluid handler.
     */
    public Optional<IFluidHandlerItemCapacity> getFluidHandlerItemCapacity(ItemStack itemStack);

    /**
     * Extract the given fluid amount from any item inside the player's inventory.
     * @param amount A fluid amount to extract.
     * @param blacklistedStack The itemstack to skip. Useful if this is the stack that you are inserting to.
     * @param fluidWhitelist A fluid to transfer, can be null to allow any fluid to be transferred.
     * @param player The player to scan the inventory from.
     * @param action The fluid action.
     * @return The extracted fluidstack.
     */
    public FluidStack extractFromInventory(int amount, @Nullable ItemStack blacklistedStack,
                                                  @Nullable Fluid fluidWhitelist, Player player,
                                                  IFluidHandler.FluidAction action);

    /**
     * Extract the given fluid amount from the given item, or from the player's inventory if that fails.
     * @param amount A fluid amount to extract.
     * @param itemStack The item to extract from first.
     * @param player The player to scan the inventory from.
     * @param action The fluid action.
     * @return The extracted fluidstack.
     */
    public FluidStack extractFromItemOrInventory(int amount, ItemStack itemStack,
                                                        @Nullable Player player,
                                                        IFluidHandler.FluidAction action);

    /**
     * Try placing or picking up fluids from the held item.
     * This can be called in {@link Item#use}.
     * @param player The active player.
     * @param hand The active hand.
     * @param world The world.
     * @param blockPos The target position.
     * @param side The target side.
     */
    public void placeOrPickUpFluid(Player player, InteractionHand hand, Level world, BlockPos blockPos, Direction side);

    /**
     * Convert a boolean-based simulate to a fluid action enum value.
     * @param simulate If in simulation mode.
     * @return The fluid action.
     */
    public IFluidHandler.FluidAction simulateBooleanToAction(boolean simulate);

}
